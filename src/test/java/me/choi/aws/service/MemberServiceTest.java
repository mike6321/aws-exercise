package me.choi.aws.service;

import me.choi.aws.domain.Member;
import me.choi.aws.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    public void 회원가입() throws Exception {
        // given
        Member member = new Member();
        member.setName("choi");
        // when
        Long savedId = memberService.join(member);
        entityManager.flush();
        // then
        assertThat(member).isEqualTo(memberRepository.findOne(savedId));
    }

    @Test
    public void 중복_회원_예외() throws Exception {
        // given
        Member member1 = new Member();
        member1.setName("choi");
        Member member2 = new Member();
        member2.setName("choi");
        // when
        memberService.join(member1);
        // then
        assertThrows(IllegalStateException.class, () -> memberService.join(member2));
    }

}
