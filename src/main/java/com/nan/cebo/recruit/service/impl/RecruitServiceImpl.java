package com.nan.cebo.recruit.service.impl;

import com.nan.cebo.competition.domain.competition.Competition;
import com.nan.cebo.competition.domain.competition.IndexPic;
import com.nan.cebo.competition.persistence.CompetitionMapper;
import com.nan.cebo.recruit.domain.pojos.RecruitInfo;
import com.nan.cebo.recruit.domain.vos.HotCompetitionVO;
import com.nan.cebo.recruit.domain.vos.RecruitBasicVO;
import com.nan.cebo.recruit.persistence.RecruitMapper;
import com.nan.cebo.recruit.service.RecruitService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author WM
 * @Date 2020/4/25 12:39
 */
@Service
public class RecruitServiceImpl implements RecruitService {

  private static final int ITEMS_NUM_OF_EACH_PAGE=6;

  @Autowired
  RecruitMapper recruitMapper;

  @Autowired
  CompetitionMapper competitionMapper;

  @Override
  public List<HotCompetitionVO> getHotCompetition() {
    List<IndexPic> indexSwiperImage = competitionMapper.getIndexSwiperImage();
    List<HotCompetitionVO> hotCompetitionVOS=indexSwiperImage.stream().map(image->{
      return convertCompetition2HotComp(competitionMapper
          .getCompetitionBasicById(image.getCompId()));
    }).distinct().collect(Collectors.toList());

    return hotCompetitionVOS;
  }

  @Override
  public List<RecruitBasicVO> getRecruitBasicAll() {
    List<RecruitInfo> recruitInfos=recruitMapper.getRecruitAll();
    List<RecruitBasicVO> recruitBasicVOs=recruitInfos.stream().map(recruitInfo -> {
      return convertRecruitInfo2VO(recruitInfo);
    }).collect(Collectors.toList());
    return recruitBasicVOs;
  }

  @Override
  public List<RecruitBasicVO> getRecruitBasic(Integer page) {
    int startIndex=(page-1)*ITEMS_NUM_OF_EACH_PAGE;
    List<RecruitInfo> recruitInfos=recruitMapper.getRecruit(startIndex);
    List<RecruitBasicVO> recruitBasicVOs=recruitInfos.stream().map(recruitInfo -> {
      return convertRecruitInfo2VO(recruitInfo);
    }).collect(Collectors.toList());
    return recruitBasicVOs;
  }

  private HotCompetitionVO convertCompetition2HotComp(Competition competition){
    if(competition==null){
      return null;
    }
    HotCompetitionVO hCompVO=new HotCompetitionVO();
    hCompVO.setCompetitionId(competition.getId());
    hCompVO.setCompetitionName(competition.getCompName());
    return hCompVO;
  }

  private RecruitBasicVO convertRecruitInfo2VO(RecruitInfo recruitInfo){
    if (recruitInfo==null){
      return null;
    }
    RecruitBasicVO recruitBasicVO=new RecruitBasicVO();
    recruitBasicVO.setId(recruitInfo.getId());
    recruitBasicVO.setCompName(recruitInfo.getCompName());
    recruitBasicVO.setWantedPerson(recruitInfo.getWantedPerson());
    return recruitBasicVO;
  }
}
