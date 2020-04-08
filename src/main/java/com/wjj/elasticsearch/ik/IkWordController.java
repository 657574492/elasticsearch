package com.wjj.elasticsearch.ik;

import com.wjj.elasticsearch.shop.dao.IkWordModelMapper;
import com.wjj.elasticsearch.shop.model.IkWordModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @author wangjunjie
 * @date 2020/4/7
 * @description ik分词 热更新
 */

@Controller
@RequestMapping("/ik")
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
                stringBuilder.append(ikWordModel.getWord());
                stringBuilder.append("\n");
            }
            String content = stringBuilder.toString();
            OutputStream out = null;
            try {
                out = response.getOutputStream();
                out.write(content.getBytes("utf-8"));
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
