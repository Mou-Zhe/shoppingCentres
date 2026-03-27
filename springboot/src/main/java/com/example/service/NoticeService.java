package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Notice;
import com.example.mapper.NoticeMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


/**
 * 业务层方法
 */
@Service
public class NoticeService {

    @Resource
    private NoticeMapper noticeMapper;



    public void add(Notice notice) {
        notice.setTime(DateUtil.now());
        noticeMapper.insert(notice);
    }

    public List<Notice> selectAll(Notice notice) {
        return noticeMapper.selectAll(notice);
    }

    public Notice selectById(Integer id) {
        return noticeMapper.selectById(id);
    }

    //分页查询的方法
    public PageInfo<Notice> selectPage(Notice notice,Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Notice> list=noticeMapper.selectAll(notice);
        return PageInfo.of(list);
    }

    public void updateById(Notice notice) {
        noticeMapper.updateById(notice);
    }

    public void deleteById(Integer id) {
        noticeMapper.deleteById(id);
    }

    public void deleteBatch(@RequestBody List<Integer> ids) {
        for (Integer id:ids){
            noticeMapper.deleteById(id);
        }
    }



}
