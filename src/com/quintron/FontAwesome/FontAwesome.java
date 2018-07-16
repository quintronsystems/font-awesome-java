/*
 * Copyright (C) 2012 Quintron Systems, LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.quintron.FontAwesome;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
/**
 * Font Awesome
 * 
 * @author mflynn
 */
public class FontAwesome {
    private static FontAwesome instance;
    public static enum Style { STRONG, REGULAR, LIGHT };
    protected float fontSize;
    protected String version = "";
    protected boolean pro = false;
    protected Style defaultStyle;
    protected Font defaultFont;
    protected Color defaultColor;
    protected Font faStrong;
    protected Font faRegular;
    protected Font faBrands;
    protected Font faLight;
    protected FontAwesomeIcons icons;
    
    public static FontAwesome getInstance(){
        if(instance == null) instance = new FontAwesome();
        return instance;
    }
    
    private void loadFonts(){
        try{
            icons = new FontAwesomeIcons();
            
            faStrong   = Font.createFont(Font.TRUETYPE_FONT,            FontAwesome.class.getResourceAsStream("Fonts/fa-solid-900.ttf")).deriveFont(Font.PLAIN, fontSize);
            faRegular  = Font.createFont(Font.TRUETYPE_FONT,            FontAwesome.class.getResourceAsStream("Fonts/fa-regular-400.ttf")).deriveFont(Font.PLAIN, fontSize);
            faBrands   = Font.createFont(Font.TRUETYPE_FONT,            FontAwesome.class.getResourceAsStream("Fonts/fa-brands-400.ttf")).deriveFont(Font.PLAIN, fontSize);
            if(pro) faLight    = Font.createFont(Font.TRUETYPE_FONT,    FontAwesome.class.getResourceAsStream("Fonts/fa-light-300.ttf")).deriveFont(Font.PLAIN, fontSize);

            setDefaultStyle(Style.STRONG);
            setDefaultColor(Color.darkGray);
            
        } catch (Exception exp) {
            exp.printStackTrace();
        }    
    }
    
    /**
     * Font Awesome 
     * @param size 
     */
    public FontAwesome(float size){
        fontSize = size;
        pro = FontAwesomeIcons.pro;
        version = FontAwesomeIcons.version;
        loadFonts();                
    }

    /**
     * Font Awesome
     */
    public FontAwesome(){
        this(16f);
    }
    
    /**
     * Set Default Style
     * 
     * @param s Style
     */
    public void setDefaultStyle(Style s){
        defaultStyle = s;
        switch (s){
            case STRONG : 
                defaultFont = faStrong;
                break;
            case REGULAR : 
                defaultFont = faRegular;
                break;
            case LIGHT : 
                defaultFont = faLight;
                break;
        }
    }
    
    /**
     * Set Default Color
     * 
     * @param c Color
     */
    public void setDefaultColor(Color c){
        defaultColor = c;
    }
    
    /**
     * Enable Java Anti-Aliasing
     */
    public static void enableAntiAliasing(){
        System.setProperty("awt.useSystemAAFontSettings","on");
        System.setProperty("swing.aatext", "true");
    }

    /**
     * Change Icon on Button
     * @param b JButton
     * @param icon 
     */
    public void updateIcon(JButton b, String icon) {
        b.setText(icons.get(icon));
        b.setForeground(defaultColor);
        b.setFont(getFont(icon));
    }

    /**
     * Change Icon on Toggle Button
     * 
     * @param b JButton
     * @param iconOn Icon On
     * @param iconOff Icon Off
     */
    public void updateIcon(JToggleButton b, String iconOn, String iconOff) {
        updateIcon(b, iconOn, defaultStyle, iconOff, defaultStyle);
    }
    
    /**
     * Change Icon on Toggle Button
     * 
     * @param b JButton
     * @param iconOn Icon On
     * @param sOn Style On
     * @param iconOff Icon Off
     * @param sOff Style Off
     */
    public void updateIcon(JToggleButton b, final String iconOn, final Style sOn, final String iconOff, final Style sOff) {
        b.setText(icons.get(iconOn));
        b.setFont(getFont(iconOn, sOn));
        b.setForeground(defaultColor);
        b.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent event) {
                JToggleButton b = (JToggleButton) event.getSource();
                if (b.isSelected()){
                    b.setText(icons.get(iconOn));
                    b.setFont(getFont(iconOn, sOn));
                } else {
                    b.setText(icons.get(iconOff));
                    b.setFont(getFont(iconOff, sOff));
                }
            }
        });
    }
    
    /**
     * Change Icon on Label
     * 
     * @param l JLabel
     * @param icon Icon
     */
    public void updateIcon(JLabel l, String icon) {
        l.setText(icons.get(icon));
        l.setForeground(defaultColor);
        l.setFont(getFont(icon));
    }
    
    /**
     * Valid Icon
     * 
     * Checks if Icon exists for a given Style
     * 
     * @param icon Icon
     * @param s Style
     * @return 
     */
    public boolean validIcon(String icon, Style s){
        if(FontAwesomeIcons.brandIcons.contains(icon)) return true;
        boolean r = false;
        switch (s){
            case STRONG : 
                r = FontAwesomeIcons.solidIcons.contains(icon);
                break;
            case REGULAR : 
                r = FontAwesomeIcons.regularIcons.contains(icon);
                break;
            case LIGHT : 
                r = FontAwesomeIcons.lightIcons.contains(icon);
                break;
        }
        return r;
    }
    
    /**
     * Get font for given Icon
     * 
     * Uses default Style
     * 
     * @param name
     * @return 
     */
    public Font getFont(String name){
        return getFont(name, defaultStyle);
    }
    
    /**
     * Get font for given Icon and Style
     * 
     * @param name
     * @param s
     * @return 
     */
    public Font getFont(String name, Style s){
        Font f;
        switch (s){
            case STRONG : 
                f = faStrong;
                break;
            case REGULAR : 
                f = faRegular;
                break;
            case LIGHT : 
                f = faLight;
                break;
            default:
                f = faStrong;
        }
        if(FontAwesomeIcons.brandIcons.contains(name)) f = faBrands;
        return f;
    }
    
    /**
     * Get Icon as JLabel
     * 
     * @param icon
     * @return 
     */
    public JLabel getIconLabel(String icon){
        return getIconLabel(icon, defaultStyle);
    }
    
    /**
     * Get Icon as JLabel
     * 
     * @param icon
     * @param s
     * @return 
     */
    public JLabel getIconLabel(String icon, Style s){
        JLabel l;
        if(validIcon(icon, s)){
            l = new JLabel(icons.get(icon));
            l.setFont(getFont(icon, s));
        }
        else {
            l = new JLabel("question-circle");
            l.setFont(faStrong);
        }
        l.setForeground(defaultColor);
        return l;
    }

    /**
     * Get Icon as JButton
     * 
     * @param icon
     * @return 
     */
    public JButton getIconButton(String icon){
        return getIconButton(icon, defaultStyle);
    }
    
    /**
     * Get Icon as JButton
     * @param icon
     * @param s
     * @return 
     */
    public JButton getIconButton(String icon, Style s){
        JButton b;
        if(validIcon(icon, s)){
            b = new JButton(icons.get(icon));
            b.setFont(getFont(icon, s));
        }
        else {
            b = new JButton("question-circle");
            b.setFont(faStrong);
        }
        b.setForeground(defaultColor);
        return b;
    }

    /**
     * Get Icon as Toggle JButton
     * 
     * Icon will toggle with state
     * 
     * @param iconOn
     * @param iconOff
     * @return 
     */
    public JToggleButton getIconToggleButton(String iconOn, String iconOff){
        return getIconToggleButton(iconOn, defaultStyle, iconOff, defaultStyle);
    }
    
    /**
     * Get Icon as Toggle JButton
     * 
     * Icon will toggle with state
     * 
     * @param iconOn
     * @param sOn
     * @param iconOff
     * @param sOff
     * @return 
     */
    public JToggleButton getIconToggleButton(final String iconOn, final Style sOn, final String iconOff, final Style sOff){
        JToggleButton b;
        b = new JToggleButton(icons.get(validIcon(iconOn, sOn) ? iconOn : "question-circle"));
        b.setFont(getFont(validIcon(iconOn, sOn) ? iconOn : "question-circle", sOn));
        b.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent event) {
                JToggleButton b = (JToggleButton) event.getSource();
                if (b.isSelected()){
                    b.setText(icons.get(validIcon(iconOn, sOn) ? iconOn : "question-circle"));
                    b.setFont(getFont(validIcon(iconOn, sOn) ? iconOn : "question-circle", sOn));
                } else {
                    b.setText(icons.get(validIcon(iconOff, sOff) ? iconOff : "question-circle"));
                    b.setFont(getFont(validIcon(iconOff, sOff) ? iconOff : "question-circle", sOff));
                }
            }
        });
        b.setForeground(defaultColor);
        return b;
    }

    /**
     * Get Strong Icon as JLabel 
     * 
     * @param icon
     * @return 
     */
    public JLabel getIconLabelStrong(String icon){
        return getIconLabel(icon, Style.STRONG);
    }

    /**
     * Get Regular Icon as JLabel
     * 
     * @param icon
     * @return 
     */
    public JLabel getIconLabelRegular(String icon){
        return getIconLabel(icon, Style.REGULAR);
    }

    /**
     * Get Light Icon as JLabel
     * 
     * @param icon
     * @return 
     */
    public JLabel getIconLabelLight(String icon){
        return getIconLabel(icon, Style.LIGHT);
    }


    /**
     * Get Strong Icon as JButton
     * 
     * @param icon
     * @return 
     */
    public JButton getIconButtonStrong(String icon){
        return getIconButton(icon, Style.STRONG);
    }

    /**
     * Get Regular Icon as JButton
     * 
     * @param icon
     * @return 
     */
    public JButton getIconButtonRegular(String icon){
        return getIconButton(icon, Style.REGULAR);
    }

    /**
     * Get Light Icon as JButton
     * 
     * @param icon
     * @return 
     */
    public JButton getIconButtonLight(String icon){
        return getIconButton(icon, Style.LIGHT);
    }    
    
    /**
     * Get Icon as ImageIcon
     * 
     * @param name
     * @return 
     */
    public ImageIcon getIcon(String name){
        return getIcon(name, defaultStyle);
    }

    /**
     * Get Icon as ImageIcon
     * 
     * @param name
     * @param size
     * @param color
     * @return 
     */
    public ImageIcon getIcon(String name, float size, Color color){
        return getIcon(name, defaultStyle, size, color);
    }
    
    /**
     * Get Icon as ImageIcon
     * 
     * @param name
     * @param color
     * @return 
     */
    public ImageIcon getIcon(String name, Color color){
        return getIcon(name, defaultStyle, color);
    }
    
    /**
     * Get Icon as ImageIcon
     * 
     * @param name
     * @param size
     * @return 
     */
    public ImageIcon getIcon(String name, float size){
        return getIcon(name, defaultStyle, size);
    }
    
    /**
     * Get Icon as ImageIcon
     * 
     * @param name
     * @param s
     * @return 
     */
    public ImageIcon getIcon(String name, Style s){
        return new ImageIcon(buildImage(name, getFont(name, s), defaultColor));
    }

    /**
     * Get Icon as ImageIcon
     * 
     * @param name
     * @param s
     * @param size
     * @return 
     */
    public ImageIcon getIcon(String name, Style s, float size){
        return new ImageIcon(buildImage(name, getFont(name, s).deriveFont(Font.PLAIN, size), defaultColor));
    }

    /**
     * Get Icon as ImageIcon
     * 
     * @param name
     * @param s
     * @param size
     * @param c
     * @return 
     */
    public ImageIcon getIcon(String name, Style s, float size, Color c){
        return new ImageIcon(buildImage(name, getFont(name, s).deriveFont(Font.PLAIN, size), c));
    }

    /**
     * Get Icon as ImageIcon
     * 
     * @param name
     * @param s
     * @param c
     * @return 
     */
    public ImageIcon getIcon(String name, Style s, Color c){
        return new ImageIcon(buildImage(name, getFont(name, s), c));
    }
    
    private BufferedImage buildImage(String name, Font font, Color color) {
        JLabel label = new JLabel(icons.get(name));
        label.setForeground(color);
        label.setFont(font);
        Dimension dim = label.getPreferredSize();
        int width = dim.width + 1;
        int height = dim.height + 1;
        label.setSize(width, height);
        BufferedImage bufImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufImage.createGraphics();
        g2d.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        label.print(g2d);
        g2d.dispose();
        return bufImage;
    }    
    
    public static void main(String[] args){
        // Demo Enviornment
        
        FontAwesome.enableAntiAliasing();
        
        FontAwesome fa = new FontAwesome();
        
        JFrame frame = new JFrame("FontAwesomeDemo");
        
        frame.setSize(1200, 1000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel title = new JPanel();
        title.add(new JLabel("Font Awesome " + fa.version + " " + (fa.pro ? "Pro" : "Free")));        
        frame.getContentPane().add(title, BorderLayout.NORTH);
                
        JPanel solidIcons = new JPanel();
        JPanel regularIcons = new JPanel();
        JPanel lightIcons = new JPanel();
        JPanel brandIcons = new JPanel();
        int w = 30;
        solidIcons.setLayout(new GridLayout((int) Math.ceil(FontAwesomeIcons.solidIcons.size() / w), w, 3, 3));        
        regularIcons.setLayout(new GridLayout((int) Math.ceil(FontAwesomeIcons.regularIcons.size() / w), w, 3, 3));        
        lightIcons.setLayout(new GridLayout((int) Math.ceil(FontAwesomeIcons.lightIcons.size() / w), w, 3, 3));        
        brandIcons.setLayout(new GridLayout((int) Math.ceil(FontAwesomeIcons.brandIcons.size() / w), w, 3, 3));        
        for(String i : FontAwesomeIcons.solidIcons){
            solidIcons.add(fa.getIconLabel(i, Style.STRONG));
        }
        for(String i : FontAwesomeIcons.regularIcons){
            regularIcons.add(fa.getIconButton(i, Style.REGULAR));
        }
        for(String i : FontAwesomeIcons.lightIcons){
            JToggleButton tb = fa.getIconToggleButton(i, Style.LIGHT, i, Style.STRONG);
            tb.setSelected(true);
            lightIcons.add(tb);
        }
        for(String i : FontAwesomeIcons.brandIcons){
            brandIcons.add(fa.getIconLabel(i, Style.STRONG));
        }
        
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));
        content.add(Box.createRigidArea(new Dimension(5,5)));
        content.add(new JLabel("Solid Icons"));
        content.add(Box.createRigidArea(new Dimension(5,5)));
        content.add(solidIcons);
        content.add(Box.createRigidArea(new Dimension(5,5)));
        content.add(new JLabel("Regular Icons"));
        content.add(Box.createRigidArea(new Dimension(5,5)));
        content.add(regularIcons);
        content.add(Box.createRigidArea(new Dimension(5,5)));
        content.add(new JLabel("Light Icons"));
        content.add(Box.createRigidArea(new Dimension(5,5)));
        content.add(lightIcons);
        content.add(Box.createRigidArea(new Dimension(5,5)));
        content.add(new JLabel("Brand Icons"));
        content.add(Box.createRigidArea(new Dimension(5,5)));
        content.add(brandIcons);

        frame.getContentPane().add(new JScrollPane(content), BorderLayout.CENTER);
        
        frame.setVisible(true);
    }
}