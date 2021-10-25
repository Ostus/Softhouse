package entities;

import java.util.Arrays;
import java.util.List;

public class Telephone extends EntityWithIdentifier {
  String mobile;
  String fixedLine;

  public Telephone(String telephoneLine) {
    List<String> allAttributes = Arrays.stream(telephoneLine.split("\\|")).toList();
    this.identifier = allAttributes.get(0);
    this.mobile = allAttributes.get(1);
    this.fixedLine = allAttributes.get(1);
  }

  public String toXmlString(Boolean inFamilyScope) {
    String extraIndentation = "";
    if (inFamilyScope) { extraIndentation = "\t"; }

    String mobileFormatted = extraIndentation + "\t\t\t<mobile>" + this.mobile + "</mobile>\n";
    String fixedLineFormatted = extraIndentation +  "\t\t\t<fixedLine>" + this.fixedLine + "</fixedLine>\n";

    return extraIndentation + "\t\t<phone>\n" + mobileFormatted + fixedLineFormatted + extraIndentation + "\t\t</phone>\n";
  }
}
