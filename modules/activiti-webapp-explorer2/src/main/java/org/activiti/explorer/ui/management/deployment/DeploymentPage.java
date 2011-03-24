/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.activiti.explorer.ui.management.deployment;

import org.activiti.explorer.ExplorerApplication;
import org.activiti.explorer.data.LazyLoadingContainer;
import org.activiti.explorer.data.LazyLoadingQuery;
import org.activiti.explorer.navigation.DeploymentNavigationHandler;
import org.activiti.explorer.navigation.UriFragment;
import org.activiti.explorer.ui.management.ManagementPage;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Table;


/**
 * @author Joram Barrez
 */
public class DeploymentPage extends ManagementPage {

  private static final long serialVersionUID = 1L;
  
  protected Table deploymentTable;
  
  protected LazyLoadingContainer deploymentListContainer;
  
  public DeploymentPage() {
    addDeploymentList();
    ExplorerApplication.getCurrent().setCurrentUriFragment(
      new UriFragment(DeploymentNavigationHandler.DEPLOYMENT_URI_PART));
  }
  
  public DeploymentPage(String deploymentId) {
    this();
    selectDeployment(deploymentListContainer.getIndexForObjectId(deploymentId));
  }
  
  protected void addDeploymentList() {
    this.deploymentTable = new Table();
    mainSplitPanel.setFirstComponent(deploymentTable);
    
    // Set non-editable, selectable and full-size
    deploymentTable.setEditable(false);
    deploymentTable.setImmediate(true);
    deploymentTable.setSelectable(true);
    deploymentTable.setNullSelectionAllowed(false);
    deploymentTable.setSizeFull();
    
    LazyLoadingQuery deploymentListQuery = new DeploymentListQuery();
    deploymentListContainer = new LazyLoadingContainer(deploymentListQuery, 10);
    deploymentTable.setContainerDataSource(deploymentListContainer);
            
    // Listener to change right panel when clicked on a deployment
    deploymentTable.addListener(new Property.ValueChangeListener() {
      private static final long serialVersionUID = 8811553575319455854L;
      public void valueChange(ValueChangeEvent event) {
        Item item = deploymentTable.getItem(event.getProperty().getValue()); // the value of the property is the itemId of the table entry
        String deploymentId = (String) item.getItemProperty("id").getValue();
        mainSplitPanel.setSecondComponent(new DeploymentDetailPanel(deploymentId));
        
        // Update URL
        ExplorerApplication.getCurrent().setCurrentUriFragment(
          new UriFragment(DeploymentNavigationHandler.DEPLOYMENT_URI_PART, deploymentId));
      }
    });
    
    // Create column headers
    deploymentTable.addContainerProperty("name", String.class, null);
    deploymentTable.setColumnHeaderMode(Table.COLUMN_HEADER_MODE_HIDDEN);
  }
  
  public void selectDeployment(int index) {
    if (deploymentTable.getContainerDataSource().size() > index) {
      deploymentTable.select(index);
      deploymentTable.setCurrentPageFirstItemId(index);
    }
  }

}