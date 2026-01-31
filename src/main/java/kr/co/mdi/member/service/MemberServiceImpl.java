package kr.co.mdi.member.service;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import kr.co.mdi.member.dao.MemberDao;
import kr.co.mdi.member.dto.MemberDTO;

@Profile("dev-user-mysql")
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberDao memberDao;

    public MemberServiceImpl(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public List<MemberDTO> findAll() {
        return memberDao.selectAllMembers();
    }

    @Override
    public Optional<MemberDTO> findById(Long id) {
        return Optional.ofNullable(memberDao.selectMemberById(id));
    }

    @Override
    public MemberDTO save(MemberDTO member) {
        if (member.getIdMember() == null) {
            return memberDao.insertMember(member);
        } else {
            return memberDao.updateMember(member);
        }
    }

    @Override
    public void delete(Long id) {
        memberDao.deleteMember(id);
    }
}
