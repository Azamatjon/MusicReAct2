package com.ReAct.MusicReAct.controller;

import com.sun.management.OperatingSystemMXBean;
import org.apache.commons.io.FileSystemUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;



@Controller
public class CPUController {

    @RequestMapping(value = "/ajax/cpu")
    @ResponseBody
    public Map<String, String> cpuAPI() {
        HashMap<String, String> map = new HashMap<>();

        OperatingSystemMXBean bean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        map.put("ProcessCpuLoad", Double.toString(bean.getProcessCpuLoad()));
        map.put("ProcessCpuTime", Long.toString(bean.getProcessCpuTime()));
        map.put("SystemCpuLoad", Double.toString(bean.getSystemCpuLoad()));

        map.put("freeMemory", Double.toString(Runtime.getRuntime().freeMemory()));
        map.put("maxMemory", Double.toString(Runtime.getRuntime().maxMemory()));
        map.put("totalMemory", Double.toString(Runtime.getRuntime().totalMemory()));
        map.put("availableProcessors", Double.toString(Runtime.getRuntime().availableProcessors()));


        try {
            map.put("diskFreeSpace", Long.toString(FileSystemUtils.freeSpaceKb(new File(".").getAbsolutePath())));
        } catch (IOException e) {
            map.put("diskFreeSpace", "-1");
        }

        return map;
    }
}
