package sakuuj.learn.library.util;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.ValidationException;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Slice;

import java.text.ParseException;

@UtilityClass
public class PaginationUtil {
    public static int parsePageSize(String pageSize, int defaultPageSize) {
        int ret;
        ret = Integer.parseInt(pageSize);

        if (ret <= 0) {
            throw new NumberFormatException("Negative value is not allowed");
        }

        return ret;
    }

    public static int getCurrentPage(String pageNumber, int startingPageNumber) {
        int page;
        try {
            page =Integer.parseInt(pageNumber);
            page -= startingPageNumber;
        } catch (NumberFormatException ex) {
            page = 0;
        }
        if (page < 0)
            page = 0;

        return page;
    }
    public static int getPrevPage(int currentPage, Slice<?> slice) {
        if (slice.hasPrevious())
            return currentPage - 1;

        return currentPage;
    }

    public static int getNextPage(int currentPage, Slice<?> slice) {
        if (slice.hasNext())
            return currentPage + 1;

        return currentPage;
    }

}
