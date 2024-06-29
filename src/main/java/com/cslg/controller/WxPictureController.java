package com.cslg.controller;

import com.cslg.api.ResultVO;
import com.cslg.bean.PictureInfo;
import com.cslg.repository.PictureRepository;
import com.cslg.utils.ApiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 小程序端轮播图
 */
@RestController
@RequestMapping("/wxPicture")
public class WxPictureController {
    @Autowired
    PictureRepository repository;

    /*
     * 返回json给小程序
     * */
    @GetMapping("/getAll")
    @ResponseBody
    public ResultVO getUserInfo() {
        List<PictureInfo> pictures = repository.findAll();
        return ApiUtil.success(pictures);
    }

}
