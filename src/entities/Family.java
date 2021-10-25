package entities;

import java.util.Arrays;
import java.util.List;

public class Family extends EntityWithIdentifier {
  String name;
  String birthYear;
  Address address;
  Telephone telephone;

  public Family(String identifier, String name, String birthYear, Address address, Telephone telephone) {
    this.identifier = identifier;
    this.name = name;
    this.birthYear = birthYear;
    this.address = address;
    this.telephone = telephone;
  }

  public Family(List<String> familyRows) {
    List<String> allFamilyAttributes = Arrays.stream(familyRows.get(0).split("\\|")).toList();
    this.identifier = allFamilyAttributes.get(0);
    this.name       = allFamilyAttributes.get(1);
    this.birthYear  = allFamilyAttributes.get(2);

    Address maybeAddress = getAddress(familyRows);
    if (maybeAddress != null) {
      this.address = maybeAddress;
    }

    Telephone maybeTelephone = getTelephone(familyRows);
    if (maybeTelephone != null) {
      this.telephone = maybeTelephone;
    }

    new Family(identifier, name, birthYear, maybeAddress, maybeTelephone);
  }

  private Address getAddress(List<String> familyLines) {
    List<String> addressLines = familyLines.stream().filter(line -> line.charAt(0) == 'A').toList();

    if (addressLines.size() > 0) {
      return new Address(addressLines.get(0));
    } else {
      return null;
    }
  }

  private Telephone getTelephone(List<String> familyLines) {
    List<String> telephoneLines = familyLines.stream().filter(line -> line.charAt(0) == 'T').toList();

    if (telephoneLines.size() > 0) {
      return new Telephone(telephoneLines.get(0));
    } else {
      return null;
    }
  }

  public String toXmlString() {
    String nameFormatted = "\t\t\t<name>" + this.name + "</name>\n";
    String birthYearFormatted = "\t\t\t<born>" + this.birthYear + "</born>\n";

    String maybeAddress = "";
    if (this.address != null) {
      maybeAddress = this.address.toXmlString(true);
    }

    String maybeTelephone = "";
    if (this.telephone != null) {
      maybeTelephone = this.telephone.toXmlString(true);
    }

    return "\t\t<family>\n" + nameFormatted + birthYearFormatted + maybeAddress + maybeTelephone + "\t\t</family>\n";
  }
}
