import exceptions.SearchException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.CommonUtil;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CommonUtilTest {

    @Test
    @DisplayName("Test SearchException is thrown")
    void testConvertSearchValueException() {
        assertThrows(SearchException.class, () -> CommonUtil.convertSearchValue(null, "test"));
    }

    @Test
    @DisplayName("Test convertSearchValue function")
    void testConvertSearchValue() throws SearchException {
        assertAll("Test conversion of search values",
                () -> assertEquals(null, CommonUtil.convertSearchValue("type", "null")),
                () -> assertEquals(15, CommonUtil.convertSearchValue("assignee_id", "15")),
                () -> assertTrue(CommonUtil.convertSearchValue("created_at", "2016-03-31T03:16:52-11:00") instanceof Date));
    }
}
