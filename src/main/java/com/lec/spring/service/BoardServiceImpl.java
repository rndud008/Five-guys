package com.lec.spring.service;

import com.lec.spring.domain.Areacode;
import com.lec.spring.domain.Attachment;
import com.lec.spring.domain.Post;
import com.lec.spring.domain.User;
import com.lec.spring.repository.*;
import com.lec.spring.util.U;
import jakarta.servlet.http.HttpSession;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;

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
    private AttachmentRepository attachmentRepository;
    private LikeRepository likeRepository;

    @Autowired
    public BoardServiceImpl(SqlSession sqlSession){
        postRepository = sqlSession.getMapper(PostRepository.class);
        userRepository = sqlSession.getMapper(UserRepository.class);
        areacodeRepository = sqlSession.getMapper(AreacodeRepository.class);
        attachmentRepository = sqlSession.getMapper(AttachmentRepository.class);
        likeRepository = sqlSession.getMapper(LikeRepository.class);
    }

    @Override
    public int write(Post post, Map<String, MultipartFile> files) {
        User user = U.getLoggedUser();

        userRepository.findById(user.getId());
        post.setUser(user);

        int cnt = postRepository.save(post);

        addFiles(files, post.getId());

        return cnt;
    }

    private void addFiles(Map<String, MultipartFile> files, Long id) {
        if(files == null) return;

        for(Map.Entry<String, MultipartFile> e : files.entrySet()){

            System.out.println("e : " + e);
            // name="upfile##" 인 경우만 첨부파일 등록
            if(!e.getKey().startsWith("upfile")) continue;

            // 물리적 파일 저장
            Attachment file = upload(e.getValue());

            // 성공시 DB 에도 파일 저장
            if(file != null){
                file.setTravel_diary_post_id(id);
                attachmentRepository.save(file);
            }
        }
    }

    private Attachment upload(MultipartFile multipartFile) {
        Attachment attachment = null;

        // 첨부된 파일 없으면 return
        String originalFilename = multipartFile.getOriginalFilename();
        if(originalFilename == null || originalFilename.isEmpty()) return null;

        // 원본파일명
        String sourceName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        // 저장파일명
        String fileName = sourceName;

        File file = new File(uploadDir, fileName);
        if(file.exists()){
            // .으로 확장자 찾기 -1이면 확장자가 없음
            int pos = fileName.lastIndexOf(".");
            if(pos > -1){
                String name = fileName.substring(0, pos);  // 파일이름
                String ext = fileName.substring(pos + 1);  // 확장자

                // ex) aaa_31415351.txt 중복방지를 위해 currentTimeMillis 사용
                fileName = name + "_" + System.currentTimeMillis() + "." + ext;

            } else {
                // ex) aaa_31415351
                fileName += "_" + System.currentTimeMillis();
            }
        }

        Path copyOfLocation = Paths.get(new File(uploadDir, fileName).getAbsolutePath());

        try {
            Files.copy(
                    multipartFile.getInputStream(),
                    copyOfLocation,
                    StandardCopyOption.REPLACE_EXISTING
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        attachment = Attachment.builder()
                .filename(fileName)
                .sourcename(sourceName)
                .build();

        return attachment;
    }

    @Override
    @Transactional
    public Post detail(Long id) {
        postRepository.incViewCnt(id);
        Post post = postRepository.findById(id);

        if(post != null){
            List<Attachment> fileList = attachmentRepository.findByPost(post.getId());
            post.setFileList(fileList);
        }

        return post;
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
            for(Post p : list){
                p.setLikecnt(likeRepository.countByPost(p.getId()));
            }
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

//    @Override
//    public List<Post> list(Integer page, Model model, Areacode areacode) {
//
//    }

    @Override
    public List<Post> listByAreacode(Integer page, Long areacode, Model model) {
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
            list = postRepository.selectFromRowArea(fromRow, pageRows, areacode);
            for(Post p : list){
                p.setLikecnt(likeRepository.countByPost(p.getId()));
            }
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
        model.addAttribute("areacode", areacode);

        return list;
    }

    @Override
    public Areacode findAreaByAreacode(Long areacode) {
        return areacodeRepository.findByAreaCode(areacode);
    }

    @Override
    public List<Areacode> findAllArea() {
        return areacodeRepository.findAllArea();
    }


    @Override
    public Post selectById(Long id) {
        Post post = postRepository.findById(id);

        if(post != null){
            List<Attachment> fileList = attachmentRepository.findByPost(post.getId());
            post.setFileList(fileList);
        }
        return post;
    }

    @Override
    public int update(Post post, Map<String, MultipartFile> files, Long[] delfile) {
        int result = 0;
        result = postRepository.update(post);

        addFiles(files, post.getId());

        if(delfile != null){
            for(Long fileId : delfile){
                Attachment file = attachmentRepository.findById(fileId);
                if(file != null){
                    delFile(file);
                    attachmentRepository.delete(file);
                }
            }
        }

        return result;
    }

    private void delFile(Attachment file) {
        String saveDirectory = new File(uploadDir).getAbsolutePath();

        File f = new File(saveDirectory, file.getFilename());  // 물리적으로 저장된 파일

        // java.io.File 에서 제공하는 함수
        if (f.exists()) {
            if (f.delete()) {
                System.out.println("삭제 성공");
            } else {
                System.out.println("삭제 실패");
            }
        } else {
            System.out.println("파일이 존재하지 않습니다");
        }
    }

    @Override
    public int deleteById(Long id) {
        int result = 0;

        Post post = postRepository.findById(id);
        if(post != null){
            List<Attachment> fileList = attachmentRepository.findByPost(id);

            if(fileList != null && fileList.size() > 0){
                for(Attachment file : fileList){
                    delFile(file);
                }
            }
            result = postRepository.delete(post);
        }
        return result;
    }
}
