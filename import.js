/**
 * Generates FontAwesomeIcons.java from the data in icons.json in a FontAwesome release
 * Place in the advanced-options/metadata folder and run with `node strip.js`
 * Author: Mike Flynn
 **/

var prompt = require("prompt");
var pro = false;
var importFolder = "Font-Awesome";
var version = 5;
var icons;
var fs;

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
    var info = require(`./${importFolder}/package.json`);
    version = info.version;
    runImport();
  }
  else {
    console.log("Using Free Version");
    prompt.get(["Version"], (err, res) => {
      version = res.version;
      runImport();      
    });
  }



});

var runImport = function() {
  icons = require(`./${importFolder}/advanced-options/metadata/icons.json`);
  fs    = require('fs-extra');

  fs.copyFile(`./${importFolder}/LICENSE.txt`, './LICENSE.txt', (err) => {
    if (err) throw err;
    console.log('Licence Copied');
  });

  fs.copyFile(`./${importFolder}/web-fonts-with-css/webfonts/fa-solid-900.ttf`, './src/com/quintron/FontAwesome/Fonts/fa-solid-900.ttf', (err) => {
    if (err) throw err;
    console.log('fa-solid-900.ttf copied');
  });

  fs.copyFile(`./${importFolder}/web-fonts-with-css/webfonts/fa-regular-400.ttf`, './src/com/quintron/FontAwesome/Fonts/fa-regular-400.ttf', (err) => {
    if (err) throw err;
    console.log('fa-regular-400.ttf copied');
  });

  fs.copyFile(`./${importFolder}/web-fonts-with-css/webfonts/fa-brands-400.ttf`, './src/com/quintron/FontAwesome/Fonts/fa-brands-400.ttf', (err) => {
    if (err) throw err;
    console.log('fa-brands-400.ttf copied');
  });

  if(fs.existsSync(`./${importFolder}/web-fonts-with-css/webfonts/fa-light-300.ttf`)){
      pro = true;
      fs.copyFile(`./${importFolder}/web-fonts-with-css/webfonts/fa-light-300.ttf`, './src/com/quintron/FontAwesome/Fonts/fa-light-300.ttf', (err) => {
        if (err) throw err;
        console.log('fa-light-300.ttf copied');
      });
  }

  var strongs   = [];
  var regulars  = [];
  var lights    = [];
  var brands    = [];

  var output = `
  package com.quintron.FontAwesome;

  import java.lang.reflect.Field;
  import java.util.Arrays;
  import java.util.ArrayList;

  public class FontAwesomeIcons {`;
  for(var k in icons){
      var v = icons[k].unicode;
      name = k.replace(/-([a-z])/g, function (g) { return g[1].toUpperCase(); }).replace("-", "").replace(/^(\d)/g, function(g){ return "_" + g[0]; });
      for(var style of icons[k].styles){
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
      public static ArrayList<String> solidIcons = new ArrayList<String>(Arrays.asList(`;
  for(var s of strongs) output += `${s == strongs[0] ? "" : ","}"${s}"`;
  output += `));
      public static ArrayList<String> regularIcons = new ArrayList<String>(Arrays.asList(`;
  for(var s of regulars) output += `${s == regulars[0] ? "" : ","}"${s}"`;
  output += `));
      public static ArrayList<String> lightIcons = new ArrayList<String>(Arrays.asList(`;
  for(var s of lights) output += `${s == lights[0] ? "" : ","}"${s}"`;
  output += `));
      public static ArrayList<String> brandIcons = new ArrayList<String>(Arrays.asList(`;
  for(var s of brands) output += `${s == brands[0] ? "" : ","}"${s}"`;
  output += `));
      public static boolean pro = ${pro ? "true" : "false"};
      public static String version = "${version}";
      public String get(String name){
          try {
              StringBuffer newName = new StringBuffer();
              for(String s : name.split("-")){
                  if(newName.length() == 0) newName.append(s);
                  else {
                      newName.append(Character.toUpperCase(s.charAt(0)));
                      if (s.length() > 1) newName.append(s.substring(1, s.length()).toLowerCase());
                  }
              }
              name = newName.toString();
              if(name.matches("^[0-9].*")) name = "_" + name;
              Field f = FontAwesomeIcons.class.getField(name);
              return (String) f.get(FontAwesomeIcons.class);
          }
          catch( Exception e){ return questionCircle; }
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
