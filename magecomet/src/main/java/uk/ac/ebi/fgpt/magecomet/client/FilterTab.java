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
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.tab.Tab;

public class FilterTab extends Tab{
	private final HStack filterStack = new HStack();
	private final FilterBuilder filterBuilder  = new FilterBuilder();
	private final ComboBoxItem columnChooserCombobox = new ComboBoxItem("Column");
	private final TextItem cellValueTextItem = new TextItem();
	private final IButton filterButton = new IButton("Filter");
	private final IButton setForVisibleButton = new IButton("Set For Visible");
	private final DynamicForm form = new DynamicForm();
	
	private GuiMediator guiMediator;

	public FilterTab(GuiMediator guiMediator){
		super();
        this.guiMediator=guiMediator;
        this.guiMediator.registerFilterTab(this);
 
        setTitle("Filter");
        filterStack.setOverflow(Overflow.VISIBLE);
        filterStack.setHeight(40);

        
    	//Build a filter
		filterBuilder.setSaveOnEnter(true);
		
		form.setNumCols(4);  
		columnChooserCombobox.setTitle("Column");  
		cellValueTextItem.setTitle("Value");
		form.setItems(columnChooserCombobox,cellValueTextItem);
		
		
		//Make a button to handle changing all values in column 
    	setForVisibleButton.setLeft(0);  
    	setForVisibleButton.setCanHover(true);
    	setForVisibleButton.addHoverHandler(new HoverHandler() {
			
			public void onHover(HoverEvent event) {
                String prompt = "Click this button to change all the values in \"" +
                columnChooserCombobox.getDisplayValue() + "\" to \"" +
                cellValueTextItem.getValueAsString()+"\"";  
                setForVisibleButton.setPrompt(prompt);
            }  
		});
    	
    	setPane(filterStack);

	}

	public void setData(final ListGrid listGrid) {
		filterBuilder.setDataSource(listGrid.getDataSource());
		filterBuilder.addSearchHandler(new SearchHandler(){
			public void onSearch(FilterSearchEvent event) {
				listGrid.fetchData(filterBuilder.getCriteria());
			}
		});
		filterButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				listGrid.filterData(filterBuilder.getCriteria());
			}
		});
		setForVisibleButton.addClickHandler(new ClickHandler() {  
    		public void onClick(ClickEvent event) {  
	//        	for(ListGridField field:sdrfTable.getAllFields()){
	//        		System.out.print(field.getTitle()+"\t");
	//        	}
    			//This updates based on what is visible
    			for(ListGridRecord record:listGrid.getRecords()){
    				String columnName = columnChooserCombobox.getValue().toString();
    				record.setAttribute(columnName, cellValueTextItem.getValue().toString());
    				listGrid.updateData(record);
    			}
    			listGrid.saveAllEdits();
    			
    			
//    			sdrfTable.getRecord(0).setAttribute("1", "Happy");
//    			sdrfTable.saveAllEdits();
//    			sdrfTable.updateData(sdrfTable.getRecord(0));
    		}
    	});
		guiMediator.updateColumnsInComboBox(listGrid.getAllFields());
		
		//Layout
		filterStack.addMember(filterBuilder);
		filterStack.addMember(filterButton);
    	filterStack.addMember(form);
    	filterStack.addMember(setForVisibleButton);
		
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