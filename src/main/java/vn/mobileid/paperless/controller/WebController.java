/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobileid.paperless.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class WebController {
   
    @RequestMapping(value = {"/single.html", "/batch.html"}, method = RequestMethod.GET)
    public String single() {
        return "forward:/index.html";
    }
    
}