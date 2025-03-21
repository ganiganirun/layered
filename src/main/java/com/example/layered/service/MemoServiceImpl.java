package com.example.layered.service;

import com.example.layered.dto.MemoRequestDto;
import com.example.layered.dto.MemoResponseDto;
import com.example.layered.entity.Memo;
import com.example.layered.repository.MemoRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


/**
 * Annotation @Service는 @Component와 같다, Spring Bean으로 등록한다는 뜻.
 * Spring Bean으로 등록되면 다른 클래스에서 주입하여 사용할 수 있다.
 * 명시적으로 Service Layer 라는것을 나타낸다.
 * 비지니스 로직을 수행한다.
 */
@Service
public class MemoServiceImpl implements MemoService{

  private final MemoRepository memoRepository;

  public MemoServiceImpl(MemoRepository memoRepository) {
    this.memoRepository = memoRepository;
  }

  @Override
  public MemoResponseDto saveMemo(MemoRequestDto dto) {

    // 요청 받은 데이터로 메모 객체 생성 id 없음
    Memo memo = new Memo(dto.getTitle(), dto.getContents());

    // db에 저장
    Memo savedMemo = memoRepository.saveMemo(memo);

    return new MemoResponseDto(savedMemo);
  }

  @Override
  public List<MemoResponseDto> findAllMemos() {

    //List<MemoResponseDto> allMemos = memoRepository.findAllMemos();
    return memoRepository.findAllMemos();
  }

  @Override
  public MemoResponseDto findMemoById(Long id) {
    Memo memo = memoRepository.findMemoById(id);

    // NPE 방지
    if (memo == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id);
    }

    return new MemoResponseDto(memo);

//    return new MemoResponseDto(memoRepository.findMemoById(id));
  }

  @Override
  public MemoResponseDto updateMemo(Long id, String title, String contents) {
    // memo 조회
    Memo memo = memoRepository.findMemoById(id);

    // NPE 방지
    if (memo == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id);
    }

    // 필수값 검증
    if (title == null || contents == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The title and content are required values.");
    }

    // memo 수정
    memo.update(title, contents);

    return new MemoResponseDto(memo);
  }

  @Override
  public MemoResponseDto updateTitle(Long id, String title, String contents) {
    Memo memo = memoRepository.findMemoById(id);

    if(memo == null){
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Does not exist id = " + id);
    }

    // 필수값 검증
    if (title == null || contents != null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The title is a required value.");
    }
    memo.updateTitle(title);

    return new MemoResponseDto(memo);
  }

  @Override
  public void deleteMemo(Long id) {
    // memo 조회
    Memo memo = memoRepository.findMemoById(id);

    // NPE 방지
    if (memo == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id);
    }

    memoRepository.deleteMemo(id);
  }

}
