package entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Person extends EntityWithIdentifier {
  String firstName;
  String lastName;
  Address address;
  Telephone telephone;
  List<Family> familyMembers;

  public Person(List<String> linesForPerson) {
    List<String> allPersonAttributes = Arrays.stream(linesForPerson.get(0).split("\\|")).toList();
    this.identifier = allPersonAttributes.get(0);
    this.firstName  = allPersonAttributes.get(1);
    this.lastName   = allPersonAttributes.get(2);

    Address maybeAddress = getAddress(linesForPerson);
    if (maybeAddress != null) {
      this.address = maybeAddress;
    }

    Telephone maybeTelephone = getTelephone(linesForPerson);
    if (maybeTelephone != null) {
      this.telephone = maybeTelephone;
    }

    this.familyMembers = getFamilyEntities(linesForPerson);
  }

  private List<Family> getFamilyEntities(List<String> allLines) {
    Stream<String> familyRows = allLines.stream().filter(line -> line.charAt(0) == 'F');
    List<Integer> indices = familyRows.map(allLines::indexOf).toList();

    ArrayList<Family> families = new ArrayList<>(List.of());
    for(int index = 0; index < indices.size(); index ++) {
      List<String> ls;
      if (index == indices.size() - 1) {
        ls = allLines.subList(indices.get(index), allLines.size());
      } else {
        ls = allLines.subList(indices.get(index), indices.get(index + 1));
      }
      families.add(new Family(ls));
    }

    return families.stream().toList();
  }

  private Address getAddress(List<String> allLinesForPerson) {
    // Check that any instance of an address isn't nested under the Family scope
    Stream<String> familyRows = allLinesForPerson.stream().filter(line -> line.charAt(0) == 'F');
    List<Integer> familyIndices = familyRows.map(allLinesForPerson::indexOf).toList();

    if (familyIndices.size() > 0) {
      Integer firstFamily = familyIndices.get(0);
      List<String> personLines  = allLinesForPerson.subList(0, firstFamily);
      List<String> addressLines = personLines.stream().filter(line -> line.charAt(0) == 'A').toList();

      if (addressLines.size() > 0) {
        return new Address(addressLines.get(0));
      } else {
        return null;
      }
    } else {
      List<String> personLines  = allLinesForPerson.subList(0, allLinesForPerson.size());
      List<String> addressLines = personLines.stream().filter(line -> line.charAt(0) == 'A').toList();

      if (addressLines.size() > 0) {
        return new Address(addressLines.get(0));
      } else {
        return null;
      }
    }
  }

  private Telephone getTelephone(List<String> allLinesForPerson) {
    // Check that any instance of telephone isn't nested under the Family scope
    Stream<String> familyRows = allLinesForPerson.stream().filter(line -> line.charAt(0) == 'F');
    List<Integer> familyIndices = familyRows.map(allLinesForPerson::indexOf).toList();

    if (familyIndices.size() > 0) {
      Integer firstFamily = familyIndices.get(0);
      List<String> personLines = allLinesForPerson.subList(0, firstFamily);
      List<String> telephoneLines = personLines.stream().filter(line -> line.charAt(0) == 'T').toList();

      if (telephoneLines.size() > 0) {
        return new Telephone(telephoneLines.get(0));
      } else {
        return null;
      }
    } else {
      return null;
    }
  }

  public String toXmlString() {
    String firstNameFormatted = "\t\t<firstName>" + this.firstName + "</firstName>\n";
    String lastNameFormatted = "\t\t<lastName>" + this.lastName + "</lastName>\n";

    String maybeAddress = "";
    if (this.address != null) {
      maybeAddress = this.address.toXmlString(false);
    }

    String maybeTelephone = "";
    if (this.telephone != null) {
      maybeTelephone = this.telephone.toXmlString(false);
    }

    String maybeFamilies = "";
    if (this.familyMembers.size() > 0) {
      List<String> aa = this.familyMembers.stream().map(Family::toXmlString).toList();
      maybeFamilies = String.join("", aa);
    }

    return "\t<person>\n" + firstNameFormatted + lastNameFormatted + maybeAddress + maybeTelephone + maybeFamilies + "\t</person>";
  }
}
