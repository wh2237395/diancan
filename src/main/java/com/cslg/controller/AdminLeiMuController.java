package com.cslg.controller;

import com.cslg.bean.Leimu;
import com.cslg.global.GlobalData;
import com.cslg.repository.LeiMuRepository;
import com.cslg.request.LeimuReq;
import com.cslg.utils.ExcelImportUtils;
import com.cslg.exception.DianCanException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

/**
 * 菜品类目
 * 编程小石头：2501902696（微信）
 */
@Controller
@RequestMapping("/leimu")
@Slf4j
public class AdminLeiMuController {

    @Autowired
    private LeiMuRepository repository;

    //类目列表
    @GetMapping("/list")
    public String list(ModelMap map) {
        List<Leimu> leimuList = repository.findAll();
        log.error("类目list={}", leimuList);
        map.put("leimuList", leimuList);
        return "leimu/list";
    }

    //类目详情页
    @GetMapping("/detail")
    public String index(@RequestParam(value = "leimuId", required = false) Integer leimuId,
                        ModelMap map) {
        if (leimuId != null) {
            Leimu leimu = repository.findById(leimuId).orElse(null);
            map.put("leimu", leimu);
        }

        return "leimu/detail";
    }

    //删除类目
    @GetMapping("/remove")
    public String remove(@RequestParam(value = "leimuId", required = false) Integer leimuId,
                         ModelMap map) {
        repository.deleteById(leimuId);
        map.put("url", "/diancan/leimu/list");
        return "zujian/success";
    }

    //添加/更新
    @PostMapping("/save")
    public String save(@Valid LeimuReq form,
                       BindingResult bindingResult,
                       ModelMap map) {
        if (bindingResult.hasErrors()) {
            map.put("msg", bindingResult.getFieldError().getDefaultMessage());
            map.put("url", "/diancan/leimu/detail");
            return "zujian/error";
        }

        Leimu leimu = new Leimu();
        try {
            if (form.getLeimuId() != null) {
                leimu = repository.findById(form.getLeimuId()).orElse(null);
            }
            BeanUtils.copyProperties(form, leimu);
            leimu.setAdminId(GlobalData.ADMIN_ID);//属于那个卖家
            repository.save(leimu);
        } catch (DianCanException e) {
            map.put("msg", e.getMessage());
            map.put("url", "/diancan/leimu/detail");
            return "zujian/error";
        }

        map.put("url", "/diancan/leimu/list");
        return "zujian/success";
    }

    /*
     * excel导入网页
     * */
    @GetMapping("/excel")
    public String excel(ModelMap map) {
        return "leimu/excel";
    }

    /*
     * 批量导入excel里的菜品类目到数据库
     * */
    @RequestMapping("/uploadExcel")
    public String uploadExcel(@RequestParam("file") MultipartFile file,
                              ModelMap map) {
        String name = file.getOriginalFilename();
        if (name.length() < 6 || !name.substring(name.length() - 5).equals(".xlsx")) {
            map.put("msg", "文件格式错误");
            map.put("url", "/diancan/leimu/excel");
            return "zujian/error";
        }
        List<Leimu> list;
        try {
            list = ExcelImportUtils.excelToFoodLeimuList(file.getInputStream());
            log.info("excel导入的list={}", list);
            if (list == null || list.size() <= 0) {
                map.put("msg", "导入失败");
                map.put("url", "/diancan/leimu/excel");
                return "zujian/error";
            }
            //excel的数据保存到数据库
            try {
                for (Leimu excel : list) {
                    if (excel != null) {
                        //如果类目type值已存在，就不再导入
                        List typeList = repository.findByLeimuType(excel.getLeimuType());
                        log.info("查询类目type是否存在typeList={}", typeList);
                        if (typeList == null || typeList.size() < 1) {
                            System.out.println("保存成功");
                            repository.save(excel);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("某一行存入数据库失败={}", e);
            }

        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", e.getMessage());
            map.put("url", "/diancan/leimu/excel");
            return "zujian/error";
        }
        map.put("url", "/diancan/leimu/list");
        return "zujian/success";
    }


}
