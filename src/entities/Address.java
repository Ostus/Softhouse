package entities;

import java.util.Arrays;
import java.util.List;

public class Address extends EntityWithIdentifier {
  String street;
  String city;
  String postalNumber;

  public Address(String addressLine) {
    List<String> allAttributes = Arrays.stream(addressLine.split("\\|")).toList();
    this.identifier = allAttributes.get(0);
    this.street = allAttributes.get(1);
    this.city = allAttributes.get(2);

    if (allAttributes.size() > 3) {
      this.postalNumber = allAttributes.get(3);
    }
  }

  public String toXmlString(Boolean inFamilyScope) {
    String extraIndentation = "";
    if (inFamilyScope) { extraIndentation = "\t"; }
    String streetFormatted = extraIndentation + "\t\t\t<street>" + this.street + "</street>\n";
    String cityFormatted = extraIndentation + "\t\t\t<city>" + this.city + "</city>\n";

    String postalNumberFormatted = "";
    if (this.postalNumber != null) {
      postalNumberFormatted = extraIndentation + "\t\t\t<postalNumber>" + this.postalNumber + "</postalNumber>\n";
    }

    return extraIndentation + "\t\t<address>\n" + streetFormatted + cityFormatted + postalNumberFormatted + extraIndentation + "\t\t</address>\n";
  }
}
