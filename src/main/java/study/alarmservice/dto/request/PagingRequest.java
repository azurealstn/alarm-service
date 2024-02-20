package study.alarmservice.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PagingRequest {

    private int totalRowCount; // 총 row 개수
    private int page; // 현재 페이지 번호
    private int size; // 한 페이지당 표시할 row 개수
    private int pageCount; // 한 번에 표시할 페이지 개수

    private boolean prev; // 다음 페이지가 있는지
    private boolean next; // 이전 페이지가 있는지
    private int totalPageCount; // 총 페이지 수
    private int startPage; // 시작 페이지
    private int endPage; // 끝 페이지

    public PagingRequest(int page, int size) {
        this.page = page == 0 ? 1 : page;
        this.size = size == 0 ? 10 : size;
    }

    public PagingRequest(int page, int size, int totalRowCount) {
        this(page, size);
        this.totalRowCount = totalRowCount;
        this.totalPageCount = ((totalRowCount - 1) / size) + 1;
        this.endPage = Math.min((((page - 1) / pageCount) + 1) * pageCount, totalPageCount);
        this.startPage = ((page - 1) / pageCount) * pageCount + 1;
        this.prev = startPage != 1;
        this.next = endPage != totalPageCount;
    }

    public PagingRequest(int page, int size, int totalRowCount, int pageCount) {
        this(page, size);
        this.totalRowCount = totalRowCount;
        this.pageCount = pageCount == 0 ? 10 : pageCount;
        this.totalPageCount = ((totalRowCount - 1) / this.size) + 1;
        this.endPage = Math.min((((this.page - 1) / pageCount) + 1) * pageCount, totalPageCount);
        this.startPage = ((this.page - 1) / pageCount) * pageCount + 1;
        this.prev = startPage != 1;
        this.next = endPage != totalPageCount;
    }

    public static PagingRequest of(int page, int size, int totalRowCount, int pageCount) {
        return new PagingRequest(page, size, totalRowCount, pageCount);
    }

    /**
     * offset 계산
     * @param page
     * @return int
     */
    public int getOffset(int page, int size) {
        return (page - 1) * size;
    }

}
