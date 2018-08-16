/**
 * Generates FontAwesomeIcons.java from the data in icons.json in a FontAwesome release
 * Author: Mike Flynn
 **/

let prompt = require("prompt");
let pro = false;
let importFolder = "Font-Awesome";
let version = 5;
let icons;
let fs;

prompt.start();

prompt.get([{
  name : 'pro',
  description : "Pro version? [Yn]",
  type: 'string',
  pattern: /^[Yn]$/,
  required: true
}], (err, result) => {


  if(result.pro == "Y"){
    console.log("Using Pro Version");
    pro = true;
    importFolder = "Font-Awesome-Pro";
    let info = require(`./${importFolder}/package.json`);
    version = info.version;
    runImport();
  }
  else {
    console.log("Using Free Version");
    prompt.get(["Version"], (err, res) => {
        if(err) throw err;
      version = res.Version;
      runImport();      
    });
  }



});

var runImport = function() {
    icons = require(`./${importFolder}/advanced-options/metadata/icons.json`);
    fs = require('fs-extra');

    fs.copyFile(`./${importFolder}/LICENSE.txt`, './LICENSE.txt', (err) => {
        if(err) throw err;
    console.log('Licence Copied');
})
    ;

    fs.copyFile(`./${importFolder}/web-fonts-with-css/webfonts/fa-solid-900.ttf`, './src/com/quintron/FontAwesome/Fonts/fa-solid-900.ttf', (err) => {
        if(err) throw err;
    console.log('fa-solid-900.ttf copied');
})
    ;

    fs.copyFile(`./${importFolder}/web-fonts-with-css/webfonts/fa-regular-400.ttf`, './src/com/quintron/FontAwesome/Fonts/fa-regular-400.ttf', (err) => {
        if(err) throw err;
    console.log('fa-regular-400.ttf copied');
})
    ;

    fs.copyFile(`./${importFolder}/web-fonts-with-css/webfonts/fa-brands-400.ttf`, './src/com/quintron/FontAwesome/Fonts/fa-brands-400.ttf', (err) => {
        if(err) throw err;
    console.log('fa-brands-400.ttf copied');
})
    ;

    if (fs.existsSync(`./${importFolder}/web-fonts-with-css/webfonts/fa-light-300.ttf`)) {
        pro = true;
        fs.copyFile(`./${importFolder}/web-fonts-with-css/webfonts/fa-light-300.ttf`, './src/com/quintron/FontAwesome/Fonts/fa-light-300.ttf', (err) => {
            if(err) throw err;
        console.log('fa-light-300.ttf copied');
    })
        ;
    }

    let strongs = [];
    let regulars = [];
    let lights = [];
    let brands = [];
    let all = {};

    let output = `
  package com.quintron.FontAwesome;

  import java.util.Arrays;
  import java.util.ArrayList;
  import java.util.HashMap;

  @SuppressWarnings("ALL")
  public class FontAwesomeIcons {`;
    for (let k in icons) {
        let v = icons[k].unicode;
        let name = k.replace(/-([a-z])/g, function (g) {
            return g[1].toUpperCase();
        }).replace("-", "").replace(/^(\d)/g, function (g) {
            return "_" + g[0];
        });
        all[k] = `\\u${v}`;
        for (let style of icons[k].styles) {


            switch (style) {
                case "brands" :
                    brands.push(name);
                    break;
                case "solid" :
                    strongs.push(name);
                    break;
                case "regular" :
                    regulars.push(name);
                    break;
                case "light" :
                    lights.push(name);
                    break;
            }
        }
        output += `
      public static String ${name} = "\\u${v}";`;
    }

    output += `
      private HashMap<String, String> allIcons = new HashMap<>();
      public FontAwesomeIcons(){`;
    for (let s in all) {
        output += `
        allIcons.put("${s}","${all[s]}");`;
    }
    output += `
      } 
  `;

    output += `
      public static ArrayList<String> solidIcons = new ArrayList<>(Arrays.asList(`;
    for (let s of strongs) output += `${s == strongs[0] ? "" : ","}"${s}"`;
    output += `));
      public static ArrayList<String> regularIcons = new ArrayList<>(Arrays.asList(`;
    for (let s of regulars) output += `${s == regulars[0] ? "" : ","}"${s}"`;
    output += `));
      public static ArrayList<String> lightIcons = new ArrayList<>(`;
    if (lights.length > 0) {
        output += `Arrays.asList(`;
        for (let s of lights) output += `${s == lights[0] ? "" : ","}"${s}"`;
        output += `)`;
    }
  output += `);
      public static ArrayList<String> brandIcons = new ArrayList<>(Arrays.asList(`;
  for(let s of brands) output += `${s == brands[0] ? "" : ","}"${s}"`;
  output += `));
      public static boolean pro = ${pro ? "true" : "false"};
      public static String version = "${version}";
      public String get(String name){
        if(!allIcons.containsKey(name)) return questionCircle;
        return allIcons.get(name);
      }
  `;
  output += "\n}";
  fs.writeFile("./src/com/quintron/FontAwesome/FontAwesomeIcons.java", output, function(err) {
      if(err) {
          return console.log(err);
      }

      console.log("The file was saved!");
  });
}
