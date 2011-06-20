package uk.ac.ebi.fgpt.magecomet.client;

public class ColumnField {
  private String uniqueName;
  private String visibleName;
  private boolean hidden;
  private boolean autofit;
  
  public ColumnField(String uniqueName, String visibleName) {
    this.setUniqueName(uniqueName);
    this.setVisibleName(visibleName);
    this.setHidden(false);
    this.setAutofit(true);
  }

  public void setUniqueName(String uniqueName) {
    this.uniqueName = uniqueName;
  }

  public String getUniqueName() {
    return uniqueName;
  }

  public void setVisibleName(String visibleName) {
    this.visibleName = visibleName;
  }

  public String getVisibleName() {
    return visibleName;
  }

  public void setHidden(boolean hidden) {
    this.hidden = hidden;
  }

  public boolean isHidden() {
    return hidden;
  }

  public void setAutofit(boolean autofit) {
    this.autofit = autofit;
  }

  public boolean isAutofit() {
    return autofit;
  }

  public ColumnField deepClone() {
    ColumnField copy = new ColumnField(uniqueName,visibleName);
    copy.setAutofit(this.isAutofit());
    copy.setHidden(this.isHidden());
    return copy;
  }
  
}
