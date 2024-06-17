package com.lec.spring.service;

import com.lec.spring.domain.Areacode;
import com.lec.spring.domain.Post;
import com.lec.spring.repository.AreacodeRepository;
import com.lec.spring.repository.PostRepository;
import com.lec.spring.repository.UserRepository;
import com.lec.spring.util.U;
import jakarta.servlet.http.HttpSession;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class BoardServiceImpl implements BoardService {

    @Value("${app.upload.path}")
    private String uploadDir;

    @Value("${app.pagination.write_pages}")
    private int WRITE_PAGES;

    @Value("${app.pagination.page_rows}")
    private int PAGE_ROWS;

    private PostRepository postRepository;
    private UserRepository userRepository;
    private AreacodeRepository areacodeRepository;

    @Autowired
    public BoardServiceImpl(SqlSession sqlSession){
        postRepository = sqlSession.getMapper(PostRepository.class);
        userRepository = sqlSession.getMapper(UserRepository.class);
        areacodeRepository = sqlSession.getMapper(AreacodeRepository.class);
    }

    @Override
    public int write(Post post) {
        return postRepository.save(post);
    }

    @Override
    public Post detail(Long id) {
        postRepository.incViewCnt(id);

        return postRepository.findById(id);
    }

    @Override
    public List<Post> list() {
        return postRepository.findAll();
    }

    @Override
    public List<Post> list(Integer page, Model model) {
        // 현재 페이지
        if(page == null || page < 1) page = 1;

        // 페이징
        // writePages: 한 [페이징] 당 몇개의 페이지
        // pageRows: 한 '페이지'에 몇개의 글
        HttpSession session = U.getSession();
        Integer writePages = (Integer) session.getAttribute("write_pages");
        if(writePages == null) writePages = WRITE_PAGES;
        Integer pageRows = (Integer) session.getAttribute("page_rows");
        if(pageRows == null) pageRows = PAGE_ROWS;
        session.setAttribute("page", page);  // 현재 페이지 번호 session 에 저장

        long cnt = postRepository.countAll();  // 전체 글 개수
        int totalPage = (int) Math.ceil(cnt / (double) pageRows);  // 총 페이지

        int startPage = 0;
        int endPage = 0;

        // 페이지의 글 목록
        List<Post> list = null;

        if(cnt > 0){

            // page 가 totalPage 보다 클때 totalPage 로 보정
            if(page > totalPage) page = totalPage;

            // fromRow: 몇번째 데이터부터
            int fromRow = (page - 1) * pageRows;

            // 페이징에 표시할 시작페이지, 마지막페이지 계산
            startPage = (((page - 1) / writePages) * writePages) + 1;
            endPage = startPage + writePages - 1;
            if (endPage >= totalPage) endPage = totalPage;
            // 해당 페이지의 글 목록 읽어오기
            list = postRepository.selectFromRow(fromRow, pageRows);
            model.addAttribute("list", list);
        } else {
            page = 0;
        }

        model.addAttribute("cnt", cnt);
        model.addAttribute("page", page);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("pageRows", pageRows);

        model.addAttribute("url", U.getRequest().getRequestURI());
        model.addAttribute("writePages", writePages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return list;
    }

    @Override
    public List<Post> list(Integer page, Model model, Areacode areacode) {
        // 현재 페이지
        if(page == null || page < 1) page = 1;

        // 페이징
        // writePages: 한 [페이징] 당 몇개의 페이지
        // pageRows: 한 '페이지'에 몇개의 글
        HttpSession session = U.getSession();
        Integer writePages = (Integer) session.getAttribute("write_pages");
        if(writePages == null) writePages = WRITE_PAGES;
        Integer pageRows = (Integer) session.getAttribute("page_rows");
        if(pageRows == null) pageRows = PAGE_ROWS;
        session.setAttribute("page", page);  // 현재 페이지 번호 session 에 저장

        long cnt = postRepository.countByArea(areacode);  // 지역별 글 개수
        int totalPage = (int) Math.ceil(cnt / (double) pageRows);  // 총 페이지

        int startPage = 0;
        int endPage = 0;

        // 페이지의 글 목록
        List<Post> list = null;

        if(cnt > 0){

            // page 가 totalPage 보다 클때 totalPage 로 보정
            if(page > totalPage) page = totalPage;

            // fromRow: 몇번째 데이터부터
            int fromRow = (page - 1) * pageRows;

            // 페이징에 표시할 시작페이지, 마지막페이지 계산
            startPage = (((page - 1) / writePages) * writePages) + 1;
            endPage = startPage + writePages - 1;
            if (endPage >= totalPage) endPage = totalPage;
            // 해당 페이지의 글 목록 읽어오기
            list = postRepository.selectFromRow(fromRow, pageRows);
            model.addAttribute("list", list);
        } else {
            page = 0;
        }

        model.addAttribute("cnt", cnt);
        model.addAttribute("page", page);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("pageRows", pageRows);

        model.addAttribute("url", U.getRequest().getRequestURI());
        model.addAttribute("writePages", writePages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return list;
    }

    @Override
    public List<Post> listByAreacode(Long areacode) {
        return postRepository.findByAreacode(areacode);
    }

    @Override
    public Areacode selectNameByAreacode(Long areacode) {
        return areacodeRepository.findByAreaCode(areacode);
    }

    @Override
    public List<Areacode> findAllAreaName() {
        return areacodeRepository.findAllAreaName();
    }


    @Override
    public Post selectById(Long id) {
        return postRepository.findById(id);
    }

    @Override
    public int update(Post post) {
        return postRepository.update(post);
    }

    @Override
    public int deleteById(Long id) {
        int result = 0;
        Post post = postRepository.findById(id);

        if(post != null){
            result = postRepository.delete(post);
        }
        return result;
    }
}
