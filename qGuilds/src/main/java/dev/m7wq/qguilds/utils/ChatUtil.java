package dev.m7wq.qguilds.utils;


import net.kyori.adventure.text.Component;


import net.kyori.adventure.text.TextComponent;






public class ChatUtil{





    private TextComponent replaceComponent(Component comp, TextComponent yesComponent, TextComponent noComponent) {

        TextComponent textComp = (TextComponent) comp;


        return textComp.replaceText(builder -> {
            builder.matchLiteral(noComponent).replacement("%no%");
        }).replaceText(builder -> {
            builder.matchLiteral(yesComponent).replacement("%yes%");
        });
    }

//    public static TextComponent createClickableText( String text, ClickEvent.Action clickAction, String clickValue) {
//        TextComponent component = new TextComponent(text);
//        component.setClickEvent(new ClickEvent(clickAction, clickValue));
//
//        return component;
//    }
//
//
//    public static TextComponent createCommandClickableText(String text, String command) {
//        return createClickableText(text, ClickEvent.Action.RUN_COMMAND, command);
//    }
//
//    public static String get(){
//        Component.text().clickEvent()
//    }
//
//    public static TextComponent hoverText(TextComponent component, String text){
//        HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT,
//                new ComponentBuilder(text).create());
//
//        component.setHoverEvent(hoverEvent);
//        return component;
//    }
//
//
//
//
//    public static TextComponent createSuggestCommandClickableText(String text, String command) {
//        return createClickableText(text, ClickEvent.Action.SUGGEST_COMMAND, command);
//    }




}

