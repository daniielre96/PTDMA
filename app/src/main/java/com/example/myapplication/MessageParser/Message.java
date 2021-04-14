package com.example.myapplication.MessageParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Message {

    /* SINONIMUS VARS */

    static final String [] CREATE_SINONIMUS = {"create", "make", "construct", "produce"};
    static final String [] MARK_SINONIMUS = {"mark", "check"};
    static final String[] UNMARK_SINONIMUS = {"unmark", "uncheck"};
    static final String[] DELETE_SINONIMUS = {"delete", "erase", "remove", "efface"};
    static final String[] MODIFY_SINONIMUS = {"modify", "edit", "change", "revise"};
    static final String[] ADD_SINONIMUS = {"add", "include", "insert"};
    static final String[] ENABLE_SINONIMUS = {"enable", "allow", "activate"};
    static final String[] DISABLE_SINONIMUS ={"disable", "disallow", "disactivate"};


    /* METHODS TO PARSE */

    static public int parseMainToDo(String message){

        if(message.contains("help")){ // HELP
            return 1;
        }
        else if(stringContains(message, UNMARK_SINONIMUS) && !message.contains("all tasks")){ // MARK TASK AS UNDONE
            Pattern pattern = Pattern.compile("task(.*?)");
            Matcher matcher = pattern.matcher(message);
            while(matcher.find()){return 3;}
        }
        else if(stringContains(message, MARK_SINONIMUS) && message.contains("undone")){ // MARK ALL TASKS AS UNDONE
            return 5;
        }
        else if(stringContains(message, MARK_SINONIMUS) && !message.contains("all tasks")){ // MARK TASK AS DONE
            Pattern pattern = Pattern.compile("task(.*?)as done");
            Matcher matcher = pattern.matcher(message);
            while(matcher.find()){return 2;}
        }
        else if(stringContains(message, MARK_SINONIMUS) && message.contains("done")){ // MARK ALL TASKS AS DONE
            return 4;
        }
        else if(stringContains(message, DELETE_SINONIMUS) && !message.contains("all tasks")){ // DELETE A TASK
            Pattern pattern = Pattern.compile("task(.*?)");
            Matcher matcher = pattern.matcher(message);
            while(matcher.find()){return 6;}
        }
        else if(stringContains(message, DELETE_SINONIMUS) && message.contains("all tasks")){ // DELETE ALL TASKS
            return 7;
        }
        else if(stringContains(message, CREATE_SINONIMUS)){ // CREATE A TASK
            return 8;
        }
        else if(stringContains(message, MODIFY_SINONIMUS)){ // MODIFY A TASK
            Pattern pattern = Pattern.compile("task(.*?)");
            Matcher matcher = pattern.matcher(message);
            while(matcher.find()){return 9;}
        }
        else if(stringContains(message, ENABLE_SINONIMUS)) { // ENABLE SOUND
            return 10;
        }
        else if(stringContains(message, DISABLE_SINONIMUS)){ // DISABLE SOUND
            return 11;
        }
        else if(message.contains(("events"))){ // GO TO EVENTS
            return 12;
        }
        else if(message.contains("shopping")){ // GO TO SHOPPING LIST
            return 13;
        }
        // QUERIES
        else if(message.contains("show") && message.contains("undone")){ // SHOW UNDONE TASKS
            return 14;
        }
        else if(message.contains("show") && message.contains("done")){ // SHOW DONE TASKS
            return 15;
        }
        else if(message.contains("show") && message.contains("all")){ // SHOW ALL TASKS
            return 16;
        }

        return 0;
    }

    static public int parseMainCreateModifyTask(String message){

        if(message.contains("help")){ // HELP
            return 1;
        }
        else if(message.contains("name")){ // SET THE NAME OF THE TASK
            Pattern pattern = Pattern.compile("task name is(.*?)");
            Matcher matcher = pattern.matcher(message);
            while(matcher.find()){return 2;}
        }
        else if(stringContains(message, CREATE_SINONIMUS)){ // CREATE THE TASK
            return 3;
        }
        return 0;
    }

    static public int parseMainEvents(String message){

        if(message.contains("help")){ // HELP
            return 1;
        }
        else if(stringContains(message, DELETE_SINONIMUS) && !message.contains("all events")){ // DELETE EVENT
            Pattern pattern = Pattern.compile("event(.*?)");
            Matcher matcher = pattern.matcher(message);
            while(matcher.find()){return 2;}
        }
        else if(stringContains(message, DELETE_SINONIMUS) && message.contains("all events")){ // DELETE ALL EVENTS FROM DAY
            Pattern pattern = Pattern.compile("from day(.*?)");
            Matcher matcher = pattern.matcher(message);
            while(matcher.find()){return 3;}
        }
        else if(stringContains(message, CREATE_SINONIMUS)){ // CREATE AN EVENT
            return 4;
        }
        else if(stringContains(message, MODIFY_SINONIMUS)){ // MODIFY AN EVENT
            Pattern pattern = Pattern.compile("event(.*?)");
            Matcher matcher = pattern.matcher(message);
            while(matcher.find()){return 5;}
        }
        else if(stringContains(message, ENABLE_SINONIMUS)) { // ENABLE SOUND
            return 6;
        }
        else if(stringContains(message, DISABLE_SINONIMUS)){ // DISABLE SOUND
            return 7;
        }
        else if(message.contains(("to do list"))){ // GO TO TO DO LIST
            return 8;
        }
        else if(message.contains("shopping")){ // GO TO SHOPPING LIST
            return 9;
        } else if (message.contains("show") && message.contains("today")){ // SHOW TODAY EVENTS
            return 10;
        }

        return 0;
    }

    static public int parseCreateModifyEvent(String message){

        if(message.contains("help")){ // HELP
            return 1;
        }
        else if(message.contains("name")){ // SET THE NAME OF THE EVENT
            Pattern pattern = Pattern.compile("event name is(.*?)");
            Matcher matcher = pattern.matcher(message);
            while(matcher.find()){return 2;}
        }
        else if(message.contains("date")){ // SET THE DATE OF THE EVENT
            Pattern pattern = Pattern.compile("event date is(.*?)");
            Matcher matcher = pattern.matcher(message);
            while(matcher.find()){return 3;}
        }
        else if(message.contains("time")){ // SET THE HOUR OF THE EVENT
            Pattern pattern = Pattern.compile("event time is(.*?)");
            Matcher matcher = pattern.matcher(message);
            while(matcher.find()){return 4;}
        }
        else if(stringContains(message, CREATE_SINONIMUS)){ // CREATE THE EVENT
            return 5;
        }
        return 0;
    }

    static public int parseMainShoppingList(String message){

        if(message.contains("help")){ // HELP
            return 1;
        }
        else if(stringContains(message, DELETE_SINONIMUS) && !message.contains("all lists")){ // DELETE A LIST
            Pattern pattern = Pattern.compile("list(.*?)");
            Matcher matcher = pattern.matcher(message);
            while(matcher.find()){return 2;}
        }
        else if(stringContains(message, DELETE_SINONIMUS) && message.contains("all lists")){ // DELETE ALL LISTS
            return 3;
        }
        else if(stringContains(message, CREATE_SINONIMUS)){ // CREATE A LIST
            return 4;
        }
        else if(stringContains(message, MODIFY_SINONIMUS)){ // MODIFY A LIST
            Pattern pattern = Pattern.compile("list(.*?)");
            Matcher matcher = pattern.matcher(message);
            while(matcher.find()){return 5;}
        }
        else if(message.contains("show me")){ // SHOW A LIST
            Pattern pattern = Pattern.compile("the list(.*?)");
            Matcher matcher = pattern.matcher(message);
            while(matcher.find()){return 6;}
        }
        else if(stringContains(message, ENABLE_SINONIMUS)) { // ENABLE SOUND
            return 7;
        }
        else if(stringContains(message, DISABLE_SINONIMUS)){ // DISABLE SOUND
            return 8;
        }
        else if(message.contains(("events"))){
            return 9;
        }
        else if(message.contains("to do list")){
            return 10;
        }

        return 0;
    }

    static public int parseCreateModifyShoppingList(String message){

        if(message.contains("help")){ // HELP
            return 1;
        }
        else if(message.contains("name")){ // SET THE NAME OF THE LIST
            Pattern pattern = Pattern.compile("list name is(.*?)");
            Matcher matcher = pattern.matcher(message);
            while(matcher.find()){return 2;}
        }
        else if(stringContains(message, CREATE_SINONIMUS)){ // CREATE THE LIST
            return 3;
        }

        return 0;
    }

    static public int parseShowShoppingList(String message){

        if(message.contains("help")){ // HELP
            return 1;
        }
        else if(stringContains(message, ADD_SINONIMUS)){ // ADD ELEMENT TO THE LIST
            Pattern pattern = Pattern.compile("insert element(.*?)");
            Matcher matcher = pattern.matcher(message);
            while(matcher.find()){return 2;}
        }
        else if(stringContains(message, DELETE_SINONIMUS)){ // DELETE ELEMENT FROM THE LIST
            Pattern pattern = Pattern.compile("delete element(.*?)");
            Matcher matcher = pattern.matcher(message);
            while(matcher.find()){return 3;}
        }

        return 0;
    }

   static private boolean stringContains(String message, String [] strings){
        System.out.println(Arrays.stream(strings).anyMatch(message::contains));
        return Arrays.stream(strings).anyMatch(message::contains);
   }

    static public String getBetweenStrings(String start, String end, String target){

        String result = null;
        Pattern pattern = Pattern.compile(start + "(.*?)" + end, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(target);

        while(matcher.find()) result = matcher.group(1);

        return result;
    }

    static public String getAfterString(String start, String target){

        String result = target.substring(target.indexOf(start) + start.length());

        return result;
    }
}
