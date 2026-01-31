package kr.co.mdi.member.dao;

import java.util.List;
import kr.co.mdi.member.dto.MemberDTO;

public interface MemberDao {
    List<MemberDTO> selectAllMembers();
    MemberDTO selectMemberById(Long id);
    MemberDTO insertMember(MemberDTO member);
    MemberDTO updateMember(MemberDTO member);
    void deleteMember(Long id);
}
