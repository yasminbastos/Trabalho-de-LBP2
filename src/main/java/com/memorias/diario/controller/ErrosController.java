package com.memorias.diario.controller;

import ch.qos.logback.core.model.Model;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrosController implements ErrorController {
        @RequestMapping("/error")
        public String handleError(HttpServletRequest request, Model model) {
            Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

            if (status != null) {
                int statusCode = Integer.parseInt(status.toString());

                switch (statusCode) {
                    case 400: return "400";
                    case 401: return "401";
                    case 404: return "404";
                    case 503: return "503";
                    case 504:return "504";
                }
            }

            return "500";
        }
    }


