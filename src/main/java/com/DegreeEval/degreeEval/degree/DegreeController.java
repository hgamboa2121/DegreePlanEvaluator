
package com.DegreeEval.degreeEval.degree;
import org.springframework.stereotype.Controller; 
import org.springframework.web.bind.annotation.RequestMapping; 
import org.springframework.web.servlet.ModelAndView; 
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays; 
import java.util.HashMap; 

import java.io.FileOutputStream;
import java.io.File;

@Controller
public class DegreeController {
    // TODO: Find out how to recieve excel files.
    @RequestMapping(value = "/degree", method=RequestMethod.POST)
    public ModelAndView processDegree(@RequestParam("file") MultipartFile file) {
        // TODO: Process excel file.
        try {
            byte[] byteFile = file.getBytes(); 

            try(FileOutputStream stream = new FileOutputStream("degree.xlsx")) {
                stream.write(byteFile); 
            }

            try{
                File excel = new File("degree.xlsx"); 
                Main m = new Main(); 

                HashMap<String, ArrayList<String>> results = m.processFile(excel.getAbsolutePath()); 
                ModelAndView mv = new ModelAndView(); 
                
                mv.addObject("needed", results.get("needed"));
                mv.addObject("betterGrades", results.get("grades")); 
                mv.addObject("attention", results.get("attention")); 
                mv.addObject("passed", results.get("passed"));

                mv.setViewName("degreeProcessed");
                
                return mv; 

            }catch(Exception e){
                return new ModelAndView("error"); 
            }
            //return new ModelAndView("degreeUpload"); 
        }catch(Exception e){
            return new ModelAndView("error"); 
        }
    }
}