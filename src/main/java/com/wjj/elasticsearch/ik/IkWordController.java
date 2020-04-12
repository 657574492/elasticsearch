package com.wjj.elasticsearch.ik;

import com.wjj.elasticsearch.shop.dao.IkWordModelMapper;
import com.wjj.elasticsearch.shop.model.IkWordModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wangjunjie
 * @date 2020/4/7
 * @description ik分词 热更新
 */

@Controller
@RequestMapping("/ik")
@Slf4j
public class IkWordController {

    @Autowired
    private IkWordModelMapper ikWordModelMapper;

    @GetMapping("/update")
    public void updateIkWords(HttpServletRequest request, HttpServletResponse response) throws IOException {

        List<IkWordModel> ikWordModels = ikWordModelMapper.listIkWord();
        if (ikWordModels == null || ikWordModels.size() == 0) {
            response.setHeader("Last-Modified", String.valueOf(1));
            response.setHeader("ETag", String.valueOf(1));
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            IkWordModel ikWordModel = ikWordModels.get(0);
            response.setHeader("Last-Modified", String.valueOf(ikWordModel.getTime()));
            response.setHeader("ETag", String.valueOf(ikWordModel.getTime()));
            response.setContentType("text/plain; charset=utf-8");
            for (int index = 0; index < ikWordModels.size(); ++index) {
                stringBuilder.append(ikWordModels.get(index).getWord());
                stringBuilder.append("\n");
            }
            String content = stringBuilder.toString();
            byte[] bytes = content.getBytes("utf-8");
            response.setContentLength(bytes.length);
            OutputStream out = null;
            try {
                out = response.getOutputStream();
                out.write(bytes);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    out.close();
                }
            }
        }
    }

}
