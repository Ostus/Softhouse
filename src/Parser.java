import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import entities.Person;

public class Parser {
  public static void main(String[] args) {
    String fileName = "test/test_data/example_data_1.txt";
    Parser parser = new Parser();

    List<String> linesFromFile = parser.readFile(fileName);
    List<Person> entities = parser.parseLineToEntity(linesFromFile);
    String formattedXml = parser.formatXml(entities);

    System.out.println(formattedXml);
  }




  private List<String> readFile(String fileName) {
    try {
      Path path = FileSystems.getDefault().getPath(fileName);
      return Files.readAllLines(path);
    } catch (IOException ioe) {
      System.out.println("IOException was thrown, error: " + ioe.getMessage());
      return List.of();
    }
  }

  private List<Person> parseLineToEntity(List<String> lines) {
    return getLinesForPerson(lines).stream().map(Person::new).toList();
  }

  private List<List<String>> getLinesForPerson(List<String> allLines) {
    Stream<String> personRows = allLines.stream().filter(line -> line.charAt(0) == 'P');
    List<Integer> indices = personRows.map(allLines::indexOf).toList();

    ArrayList<List<String>> rowsPerPerson = new ArrayList<>(List.of());
    for(int index = 0; index < indices.size(); index ++) {
      if (index == indices.size() - 1) {
        rowsPerPerson.add(allLines.subList(indices.get(index), allLines.size()));
      } else {
        rowsPerPerson.add(allLines.subList(indices.get(index), indices.get(index + 1)));
      }
    }

    return rowsPerPerson.stream().toList();
  }

  private String formatXml(List<Person> entities) {
    List<String> xmlEntities = entities.stream().map(Person::toXmlString).toList();
    String formattedEntities = String.join("\n", xmlEntities);
    return "<people>\n" + formattedEntities + "\n<people>";
  }
}
