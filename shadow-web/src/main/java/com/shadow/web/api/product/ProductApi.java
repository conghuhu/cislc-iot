package com.shadow.web.api.product;

import com.alibaba.fastjson.JSONObject;
import com.shadow.web.model.core.Permission;
import com.shadow.web.model.params.ProductParams;
import com.shadow.web.model.product.Product;
import com.shadow.web.model.result.ApiResult;
import com.shadow.web.model.result.ProductInfo;
import com.shadow.web.model.result.Result;
import com.shadow.web.service.FileService;
import com.shadow.web.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @Auther: wangzhendong
 * @Date: 2019/10/22 16:04
 * @Description: 产品信息操作API
 */
@Controller
@Slf4j
@RequestMapping("/admin/product")
public class ProductApi {

    @Autowired
    FileService fileService;

    @Autowired
    ProductService productService;

    /**
     * @return
     * @description 新增产品定义
     * @auther wangzhendong
     * @date 2019/10/22 16:20
     */
    @CrossOrigin
    @PostMapping("create")
    public ApiResult createProduct(
            @RequestParam("deviceId") Integer deviceId,
            @RequestParam("name") String name,
            @RequestParam("encryption") String encryption,
            @RequestParam("operateSystem") String operateSystem,
            @RequestParam("protocolList") List<String> protocolList,
            @RequestParam("serverList") List<String> serverList,
            @RequestParam("description") String description) {

        //获取参数信息，新家产品信息
        /*String file_url = null;
        try {
            file_url = fileService.saveFile(file);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("存储文件错误！");
            return ApiResult.returnError(1, "存储文件错误！");
        }*/
        Product product = new Product(name, deviceId, encryption,operateSystem,protocolList.size(), serverList.size(), description);
        Result ret = productService.insertProduct(product, protocolList, serverList);
        if (!ret.success()) {
            log.error(ret.msg());
            return ApiResult.returnError(1, "存储文件错误！");
        }
        return ApiResult.returnSuccess("");
    }

    /**
     * 获取产品列表
     *
     * @return
     */
    @PostMapping("list")
    public ApiResult selectList() {
        Result<List<ProductInfo>> ret = productService.selectProduct();
        return ApiResult.returnSuccess(JSONObject.toJSONString(ret.value()));
    }

    /**
     * 删除产品信息
     *
     * @param id
     * @return
     */
    @PostMapping("delete")
    public ApiResult deleteProduct(@RequestBody int id) {
        Result ret = productService.deleteProduct(id);
        if (!ret.success()) {
            log.error(ret.msg());
            return ApiResult.returnError(1, ret.msg());
        }
        return ApiResult.returnSuccess("");
    }

    /**
     * 修改产品信息
     *
     * @param params
     * @return
     */
    @PostMapping("update")
    public ApiResult updateProduct(@RequestBody ProductParams params) {
        Product product = params.getProduct();
        Result ret = productService.updateProduct(product, params.getProtocolList(), params.getServerList());
        if (!ret.success()) {
            log.error(ret.msg());
            return ApiResult.returnError(1, "更新错误！");
        }
        return ApiResult.returnSuccess("");
    }

    /**
     * @param id 产品id
     * @Description 产品打包
     * @author szh
     * @Date 2019/11/10 17:09
     */
    @GetMapping("/package/download")
    public void packageProduct(@RequestParam int id,
                               HttpServletRequest request,
                               HttpServletResponse response) {
        productService.packageProduct(id, request, response);
    }


    /**
     * -----------------------------------------------------测试--------------------------------------------------
     **/
    @GetMapping("/test/file")
    public void saveFile(@RequestParam("file") MultipartFile file) {
        String name = null;
        try {
            name = fileService.saveFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
