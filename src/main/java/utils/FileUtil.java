package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import pojo.Pair;
import pojo.Ticket;
import pojo.User;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

public class FileUtil {

    private FileUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static String readFile(String fileName) throws IOException
    {
        String result = null;
        ClassLoader classLoader = CommonUtil.class.getClassLoader();

        try (InputStream inputStream = classLoader.getResourceAsStream(fileName)) {

            result = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Pair convertJsonToPojo(String usersFileName, String ticketsFileName) {
        Set<User> users;
        Set<Ticket> tickets;
        try {
            String usersFileContent = FileUtil.readFile(usersFileName);
            String ticketsFileContent = FileUtil.readFile(ticketsFileName);
            ObjectMapper objectMapper = new ObjectMapper();
            users = objectMapper.readValue(usersFileContent, new TypeReference<HashSet<User>>() {
            });
            tickets = objectMapper.readValue(ticketsFileContent, new TypeReference<HashSet<Ticket>>() {
            });
            return new Pair(users, tickets);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
