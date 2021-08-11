package io.github.usalko.tus.test;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.desair.tus.server.TusFileUploadService;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/videos")
public class FileUploadController {

    @Autowired
    private TusFileUploadService tusFileUploadService;

    @RequestMapping(value = {""}, method = {RequestMethod.POST})
    public void create(final HttpServletRequest servletRequest, final HttpServletResponse servletResponse) throws IOException {
        throw new NotImplementedException();
    }

    @RequestMapping(value = {"/{id}/upload", "/**"}, method = {RequestMethod.POST, RequestMethod.PATCH, RequestMethod.HEAD,
            RequestMethod.DELETE, RequestMethod.OPTIONS, RequestMethod.GET})
    public void processUpload(final HttpServletRequest servletRequest, final HttpServletResponse servletResponse) throws IOException {
        tusFileUploadService.process(servletRequest, servletResponse);
    }

}