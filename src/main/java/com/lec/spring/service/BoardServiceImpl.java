package com.lec.spring.service;

import com.lec.spring.domain.Areacode;
import com.lec.spring.domain.Post;
import com.lec.spring.repository.AreacodeRepository;
import com.lec.spring.repository.PostRepository;
import com.lec.spring.repository.UserRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardServiceImpl implements BoardService {

    private PostRepository postRepository;
    private UserRepository userRepository;
    private AreacodeRepository areacodeRepository;

    @Autowired
    public BoardServiceImpl(SqlSession sqlSession){
        postRepository = sqlSession.getMapper(PostRepository.class);
        userRepository = sqlSession.getMapper(UserRepository.class);
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
    public List<Post> listByArea(Long areacode) {
        return postRepository.findByAreacode(areacode);
    }

    @Override
    public Areacode selectNameByAreacode(Long areacode) {
        return areacodeRepository.findByAreaCode(areacode);
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
