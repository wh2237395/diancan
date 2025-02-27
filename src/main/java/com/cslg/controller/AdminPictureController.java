package com.cslg.controller;

import com.cslg.api.ResultVO;
import com.cslg.bean.PictureInfo;
import com.cslg.exception.DianCanException;
import com.cslg.request.PictureForm;
import com.cslg.repository.PictureRepository;
import com.cslg.utils.ApiUtil;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;

/**
 * 用户相关
 */
@Controller
@RequestMapping("/picture")
@Slf4j
public class AdminPictureController {

    @Autowired
    PictureRepository repository;

    /*
     * 页面相关
     * */
    @GetMapping("/list")
    public String list(ModelMap map) {
        List<PictureInfo> pictures = repository.findAll();
        map.put("categoryList", pictures);
        return "picture/list";
    }

    @GetMapping("/index")
    public String index(@RequestParam(value = "picId", required = false) Integer picId,
                        ModelMap map) {
        PictureInfo picture = repository.findByPicId(picId);
        map.put("category", picture);
        return "picture/index";
    }

    //删除轮播图
    @GetMapping("/remove")
    public String remove(@RequestParam(value = "picId", required = false) Integer picId,
                         ModelMap map) {
        repository.deleteById(picId);
        map.put("url", "/diancan/picture/list");
        return "zujian/success";
    }

    //保存/更新
    @PostMapping("/save")
    public String save(@Valid PictureForm form,
                       BindingResult bindingResult,
                       ModelMap map) {
        log.info("SellerForm={}", form);
        if (bindingResult.hasErrors()) {
            map.put("msg", bindingResult.getFieldError().getDefaultMessage());
            map.put("url", "/diancan/picture/index");
            return "zujian/error";
        }
        PictureInfo picture = new PictureInfo();
        try {
            if (form.getPicId() != null) {
                picture = repository.findByPicId(form.getPicId());
            }
            BeanUtils.copyProperties(form, picture);
            repository.save(picture);
        } catch (DianCanException e) {
            map.put("msg", e.getMessage());
            map.put("url", "/diancan/picture/index");
            return "zujian/error";
        }

        map.put("url", "/diancan/picture/list");
        return "zujian/success";
    }


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
