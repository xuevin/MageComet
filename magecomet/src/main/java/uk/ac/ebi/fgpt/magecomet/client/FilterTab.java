package uk.ac.ebi.fgpt.magecomet.client;

import java.util.LinkedHashMap;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.HoverEvent;
import com.smartgwt.client.widgets.events.HoverHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FilterBuilder;
import com.smartgwt.client.widgets.form.events.FilterSearchEvent;
import com.smartgwt.client.widgets.form.events.SearchHandler;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.tab.Tab;

public class FilterTab extends Tab{
	private final HStack filterStack = new HStack();
	private FilterBuilder filterBuilder;
	private final ComboBoxItem columnChooserCombobox = new ComboBoxItem("Column");
	private final TextItem cellValueTextItem = new TextItem();
	private IButton filterButton;
	private final IButton replaceButton = new IButton("Replace");
	private final DynamicForm form = new DynamicForm();
	
	private GuiMediator guiMediator;

	public FilterTab(GuiMediator guiMediator){
		super();
        this.guiMediator=guiMediator;
        this.guiMediator.registerFilterTab(this);
 
        setTitle("Filter");
        filterStack.setOverflow(Overflow.VISIBLE);
        filterStack.setHeight(40);

        
    	
		
		form.setNumCols(4);  
		columnChooserCombobox.setTitle("Column");  
		cellValueTextItem.setTitle("Value");
		form.setItems(columnChooserCombobox,cellValueTextItem);
		
		
		//Make a button to handle changing all values in column 
    	replaceButton.setLeft(0);  
    	replaceButton.setCanHover(true);
    	replaceButton.addHoverHandler(new HoverHandler() {
			
			public void onHover(HoverEvent event) {
                String prompt = "Click this button to change all the values in \"" +
                columnChooserCombobox.getDisplayValue() + "\" to \"" +
                cellValueTextItem.getValueAsString()+"\"";  
                replaceButton.setPrompt(prompt);
            }  
		});
    	
    	setPane(filterStack);

	}
	public void setData(final ListGrid listGrid) {
		//Clear all previous filters
		filterStack.removeMembers(filterStack.getMembers());
		//Make a new filter builder
		filterBuilder  = new FilterBuilder();
		filterBuilder.setSaveOnEnter(true);
		filterBuilder.setDataSource(listGrid.getDataSource());
		filterBuilder.addSearchHandler(new SearchHandler(){
			public void onSearch(FilterSearchEvent event) {
				listGrid.fetchData(filterBuilder.getCriteria());
			}
		});
		
		//Make a filter filter Button
		filterButton = new IButton("Filter");
		filterButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				listGrid.filterData(filterBuilder.getCriteria());
			}
		});
		
		
		replaceButton.addClickHandler(new ClickHandler() {  
    		public void onClick(ClickEvent event) {  
    			//This updates based on what is filtered
    			for(ListGridRecord record:listGrid.getRecords()){
    				String columnName = columnChooserCombobox.getValue().toString();
    				record.setAttribute(columnName, cellValueTextItem.getValue().toString());
    				listGrid.updateData(record);
    			}
    			listGrid.saveAllEdits();
    		}
    	});
		
		guiMediator.updateColumnsInComboBoxes(listGrid.getAllFields());
		
		//Layout
		filterStack.addMember(filterBuilder);
		filterStack.addMember(filterButton);
    	filterStack.addMember(form);
    	filterStack.addMember(replaceButton);
    	filterStack.redraw();
		
	}
	/**
	 * Updates the combo box. Should be called every time the table changes
	 * @param valueMap 
	 */
	public void updateColumnsInComboBox(LinkedHashMap<String, String> valueMap) {
		if(columnChooserCombobox!=null){
			columnChooserCombobox.setValueMap(valueMap);
		}
	}
}
