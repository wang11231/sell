package com.art.sell.handler;
import com.art.sell.pojo.Msg;
import com.art.sell.util.DateUtil;
import com.art.sell.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * 自定义的logout功能
 * @author Administrator
 */
public class MyLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

	private static Logger logger  = LoggerFactory.getLogger(MyLogoutSuccessHandler.class);

	@CrossOrigin(allowCredentials = "true")
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {


		String token = request.getHeader("token");
		//用户退出的逻辑
		logger.debug("get Token:{}",token);
		HttpSession session = request.getSession(false);
		if(session != null) {
			session.invalidate();
			logger.debug("{} invalidated",session.getId());
		}


		//从redis中删除数据

		Msg msg = new Msg(Msg.SUCCESS_CODE,"logout-success",null, DateUtil.formatDate(new Date()));
		response.setContentType("application/json;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.write(JsonUtil.objectTojson(msg));
		out.flush();
		out.close();
		return;
	}
}
