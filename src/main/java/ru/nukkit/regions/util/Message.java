package ru.nukkit.regions.util;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Location;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;

import java.io.File;
import java.text.DecimalFormat;


public enum Message {

	//Default (lang) messages
	LNG_LOAD_FAIL ("Failed to load languages from file. Default message used"),
	LNG_SAVE_FAIL ("Failed to save lang file"),
	LNG_PRINT_FAIL ("Failed to print message %1%. Sender object is null."),
	LNG_CONFIG ("[MESSAGES] Messages: %1% Language: %2% Save translate file: %1% Debug mode: %3%"),
	WORD_UNKNOWN ("Unknown"),
	WRONG_PERMISSION("You have not enough permissions to execute this command"),
	PERMISSION_FAIL ("You have not enough permissions to execute this command",'c'),
	PLAYER_COMMAD_ONLY ("You can use this command in-game only!",'c'),
    CMD_REGISTERED("Command registered: %1%"),
    HLP_TITLE("RegionsPlugin %1%"),

	CMD_WAND_DESC("Give player wand item"),
	SEL_DESC("Toggle selection mode"),
    SEL_ENABLE("Selection mode enabled!"),
    SEL_DISABLE("Selection mode disabled!"),
    SEL_OK("Point #%2% selected: %1%"),
    SEL_FAIL("Failed to select point"),
    RG_DEFINE_DESC("Define new region"),
    DEF_SELECT("Failed to define region. Area was not selected"),
    RG_DEF_ID_USED("Region %1% already defined"),
    RG_DEF_OK("Region %1% created!"),
    RG_DEF_FAIL("Failed to define region %1%"),
    RG_INFO_DESC("/region info <id> - show region information about region"),
    UNKNOWN_REGION("Unknown region %1%",'c','4'),
    RG_INFO_REGNAME("==== Region: %1% ====",'6','e'),
    RG_INFO_COORD("Dimensions: %1%"), RG_INFO_OWNERS("Owners: %1%"),
    RG_INFO_MEMBERS("Members: %1%"),
    RG_LIST_DESC("/region list [pageNum] - list all regions"),
    RG_LIST("Region list: %1%"),
    RG_CHK_DESC("/region info - check and list all regions around you"),
    RG_CHK_NOTFOUND("There's no regions around you!"),
    RG_CHK_FOUND("RegionsPlugin around you: %1%"),
    RG_FLAG_DESC("/rg flag <region> <flag> [rel:<relation>] <value> - configure flag for region"),
    UNKNOWN_FLAG("Unknown flag type: %1%",'c','4'),
    RG_FLAG_FAIL("Failed to set flag %1% check syntax and try again",'c','4'),
    RG_FLAG_OK("Flag %1% successfully set"),
    RG_SETOWN_DESC("/region setowner <id> <owner>[, <owner2>..] - set owner of region (previous owners will be removed)"),
    RG_ADDOWN_DESC("/region addowner <id> <owner>[, <owner2>..] - add owner(s) to defined region"),
    RG_SETOWN_FAIL("Failed to set owners of region %1%: %2%",'c','4'),
    RG_SETOWN_OK("Successfully set owners of region %1%: %2%"),
    RG_ADDOWN_FAIL("Failed to add owners to region %1%: %2%",'c','4'),
    RG_ADDOWN_OK("Successfully added owners to region %1%: %2%"),
    RG_HELP_DESC("/region help <page> - show plugin's help"),
    RG_REMOVE_DESC("/region remove <id> - remove defined region"),
    RG_REMOVE_OK("Region %1% successfully removed!"),
    RG_REMOVE_FAIL("Failed to remove region %1%!",'c','4'),
    RG_REDEFINE_DESC("/region define <id> - change dimensions of region according to current selection"),
    RG_REDEF_FAIL("Failed to redefine region %1%"),
    RG_REDEF_OK("Region %1% redefined!"),
    RG_FLAG_CLEAR_OK("Flag %1% successfully removed from region %2%"),
    RG_FLAG_CLEAR_FAIL("Failed to remove flag %1% from region %2%"),
    SEL_POS_DESC("/select <pos1|pos2> - select current player location as point#1 or point#2"),
    RG_SETMBMR_DESC("/region setmember <id> <member>[, <member2>..] - set members of region (previous members will be removed)"),
    RG_SETMBMR_FAIL("Failed to set members of region %1%: %2%",'c','4'),
    RG_SETMBMR_OK("Successfully set members of region %1%: %2%"),
    RG_ADDMBMR_DESC("/region addmember <id> <member>[, <member2>..] - add member(s) to defined region"),
    RG_ADDMBMR_FAIL("Failed to add members to region %1%: %2%",'c','4'),
    RG_ADDMBMR_OK("Successfully added members to region %1%: %2%"),
    RG_CLAIM_DESC("/region claim <region> - create region and add yourself to owners"),
    RG_REMMEM_DESC("/region removemember <id> <player> - remove member from region"),
    RG_REMMEM_OK("Player %1% was removed from members of region %2%"),
    RG_REMMEM_FAIL("Failed to remove %1% from members of region %2%"),
    RG_REMOWN_DESC("/region removowner <id> <player> - remove owner from region"),
    RG_REMOWN_OK("Player %1% was removed from owners of region %2%"),
    RG_REMOWN_FAIL("Failed to remove %1% from owners of region %2%"),
    ONLY_OWNER("Only owner of regions can perform this operation!",'c'),
    RG_CLAIM_INSIDE_ONLY("You can claim regions only inside specified areas!",'c'),
    RG_CLAIM_SINGLE_ONLY ("Cannot claim multiple regions",'c'),
    RG_CLAIM_ALREADY ("This regions is already claimed"),
    RG_CLAIM_EXIST_OK("You successfully claimed region %1%"),
    RG_CLAIM_EXIST_FAIL("Failed to claim region %1%",'c','4'),
    RG_CLAIM_EXIST_UNALLOWED("You cannot claim region %1%",'c','4'),
    RG_CLAIM_EXIST_INSIDE("You must be inside region you want to claim. Move into that region and type /claim"),
    RG_CLAIM_OK ("Region claimed %1%"),
    RG_CLAIM_FAIL ("Failed to claim region %1%",'c','4'),
    RG_CLAIM_MAX_COUNT_REACHED ("You cannot claim region. Only %1% regions per player!",'c','4'),
    RG_CLAIM_AREA_VOLUME_REACHED("Selected area is too large to claim! You can claim %1% blocks only!",'c','4'),
    RG_CLAIM_AREA_INTERSECTED("Cannot claim selected area. It's already used by another region!",'c','4'),
    RG_CLAIM_NEED_ID ("You must provide region id to claim. Use /claim <id>",'c'),


    FMSG_INTERACT ("You cannot interact with this object!",'5'),
    FMSG_BREAK ("You cannot break blocks here!",'5'),
    FMSG_BUILD ("You cannot place blocks here!",'5'),
    FMSG_ENTRY("You cannot pass through this region!",'5'),
    FMSG_LEAVE("You cannot leave this region!",'5'),

    RG_RELOAD_DESC("/region reload - reload plugin configuration and regions"),
    RG_RELOAD_OK("Cofiguration reloaded. Loaded %1% regions"),

    SEL_SHOW_DESC("/select show - toggle displaying selected area using particles"),
    SEL_SHOW_DISABLED("Selections particles disabled in config. Command canceled.",'c'),
    SEL_SHOW_PLR_ENABLED("Selections show mode enabled"),
    SEL_SHOW_PLR_DISABLED("Selections show mode disabled");;

    private static boolean debugMode = false;
    private static String language = "english";
    private static char c1 = 'a';
    private static char c2 = '2';

    private static PluginBase plugin = null;

    /**
     * This is my favorite debug routine :) I use it everywhere to print out variable values
     * @param s - array of any object that you need to print out.
     * Example:
     * Message.BC ("variable 1:",var1,"variable 2:",var2)
     */
    public static void BC (Object... s){
        if (!debugMode) return;
        if (s.length==0) return;
        StringBuilder sb = new StringBuilder("&3[").append(plugin.getDescription().getName()).append("]&f ");
        for (Object str : s)
            sb.append(str.toString()).append(" ");
        plugin.getServer().broadcastMessage(TextFormat.colorize(sb.toString().trim()));
    }



    /**
     * Send current message to log files
     * @param s
     * @return — always returns true.
     * Examples:
     * Message.ERROR_MESSAGE.log(variable1); // just print in log
     * return Message.ERROR_MESSAGE.log(variable1); // print in log and return value true
     */
    public boolean log(Object... s){
        plugin.getLogger().info(getText (s));
        return true;
    }

    /**
     * Same as log, but will printout nothing if debug mode is disabled
     * @param s
     * @return — always returns true.
     */
    public boolean debug(Object... s){
        if (debugMode) plugin.getLogger().info(TextFormat.clean(getText (s)));
        return true;
    }

    /**
     * Show a message to player in center of screen (this routine unfinished yet)
     * @param seconds — how much time (in seconds) to show message
     * @param sender — Player
     * @param s
     * @return — always returns true.
     */
    public boolean tip (int seconds, CommandSender sender, Object... s){
        if (sender == null) return Message.LNG_PRINT_FAIL.log(this.name());
        final Player player = sender instanceof Player ? (Player) sender : null;
        final String message = getText(s);
        if (player==null) sender.sendMessage(message);
        else for (int i=0;i<seconds;i++) Server.getInstance().getScheduler().scheduleDelayedTask(new Runnable() {
            public void run() {
                if (player.isOnline()) player.sendTip(message);
            }
        },20*i);
        return true;
    }

    /**
     * Show a message to player in center of screen
     * @param sender — Player
     * @param s
     * @return — always returns true.
     */
    public boolean tip (CommandSender sender, Object... s){
        if (sender == null) return Message.LNG_PRINT_FAIL.log(this.name());
        Player player = sender instanceof Player ? (Player) sender : null;
        String message = getText(s);
        if (player==null) sender.sendMessage(message);
        else player.sendTip(message);
        return true;
    }

    /**
     * Send message to Player or to ConsoleSender
     * @param sender
     * @param s
     * @return — always returns true.
     */
    public boolean print (CommandSender sender, Object... s){
        if (sender == null) return Message.LNG_PRINT_FAIL.log(this.name());
        sender.sendMessage(getText(s));
        return true;
    }

    /**
     * Send message to all players or to players with defined permission
     * @param permission
     * @param s
     * @return — always returns true.
     *
     * Examples:
     * Message.MSG_BROADCAST.broadcast ("pluginname.broadcast"); // send message to all players with permission "pluginname.broadcast"
     * Message.MSG_BROADCAST.broadcast (null); // send message to all players
     */
    public boolean broadcast (String permission, Object... s){
        for (Player player : plugin.getServer().getOnlinePlayers().values()){
            if (permission==null || player.hasPermission(permission)) print (player,s);
        }
        return true;
    }


    /**
     * Get formated text.
     * @param keys
     *
     ** Keys - are parameters for message and control-codes.
     * Parameters will be shown in position in original message according for position.
     * This keys are used in every method that prints or sends message.
     *
     * Example:
     *
     * EXAMPLE_MESSAGE ("Message with parameters: %1%, %2% and %3%");
     * Message.EXAMPLE_MESSAGE.getText("one","two","three"); //will return text "Message with parameters: one, two and three"
     *
     ** Color codes
     * You can use two colors to define color of message, just use character symbol related for color.
     *
     * Message.EXAMPLE_MESSAGE.getText("one","two","three",'c','4');  // this message will be red, but word one, two, three - dark red
     *
     ** Control codes
     * Control codes are text parameteres, that will be ignored and don't shown as ordinary parameter
     * - "SKIPCOLOR" - use this to disable colorizing of parameters
     * - "NOCOLOR" (or "NOCOLORS") - return uncolored text, clear all colors in text
     * - "FULLFLOAT" - show full float number, by default it limit by two symbols after point (0.15 instead of 0.1483294829)
     *
     * @return
     */
    public String getText (Object... keys){
        char [] colors = new char[]{color1 == null ? c1 : color1 , color2 == null ? c2 : color2};
        if (keys.length ==0) return TextFormat.colorize("&"+ colors[0] +this.message);
        String str = this.message;
        boolean noColors = false;
        boolean skipDefaultColors = false;
        boolean fullFloat = false;
        String prefix = "";
        int count=1;
        int c = 0;
        DecimalFormat fmt = new DecimalFormat("####0.##");
        for (int i = 0; i<keys.length; i++){
            String s = keys[i].toString();
            if (c<2&&keys[i] instanceof Character){
                colors[c] = (Character) keys[i];
                c++;
                continue;
            } else if (s.startsWith("prefix:")) {
                prefix = s.replace("prefix:", "");
                continue;
            } else if (s.equals("SKIPCOLOR")) {
                skipDefaultColors = true;
                continue;
            } else if (s.equals("NOCOLORS")||s.equals("NOCOLOR")) {
                noColors = true;
                continue;
            } else if (s.equals("FULLFLOAT")) {
                fullFloat = true;
                continue;
            } else if (keys[i] instanceof Location) {
                Location loc = (Location) keys[i];
                if (fullFloat) s = loc.getLevel().getName()+"["+loc.getX()+", "+loc.getY()+", "+loc.getZ()+"]";
                else s = loc.getLevel().getName()+"["+fmt.format(loc.getX())+", "+fmt.format(loc.getY())+", "+fmt.format(loc.getZ())+"]";
            } else if (keys[i] instanceof Double || keys[i] instanceof Float) {
                if (!fullFloat) s = fmt.format((Double) keys[i]);
            }

            String from = (new StringBuilder("%").append(count).append("%")).toString();
            String to = skipDefaultColors ? s :(new StringBuilder("&").append(colors[1]).append(s).append("&").append(colors[0])).toString();
            str = str.replace(from, to);
            count++;
        }
        str = TextFormat.colorize(prefix.isEmpty() ? "&" + colors[0] + str : prefix + " " + "&" + colors[0] + str);
        if (noColors) str = TextFormat.clean(str);
        return str;
    }

    private void initMessage (String message){
        this.message = message;
    }

    private String message;
    private Character color1;
    private Character color2;
    Message (String msg){
        message = msg;
        this.color1 = null;
        this.color2 = null;
    }
    Message (String msg, char color1, char color2){
        this.message = msg;
        this.color1 = color1;
        this.color2 = color2;
    }
    Message (String msg, char color){
        this (msg,color,color);
    }

    @Override
    public String toString(){
        return this.getText("NOCOLOR");
    }

    /**
     * Initialize current class, load messages, etc.
     * Call this file in onEnable method after initializing plugin configuration
     * @param plg
     */
    public static void init(PluginBase plg){
        init (plg, false);
    }
    public static void init(PluginBase plg, boolean createCfg){
        plugin = plg;
        language = plg.getConfig().getString("general.language","default");
        if (language.equalsIgnoreCase("default")) language = Server.getInstance().getLanguage().getLang();
        debugMode = plg.getConfig().getBoolean("general.debug-mode",false);
        if (createCfg) {
            plg.getDataFolder().mkdirs();
            plg.getConfig().set("general.language", language);
            plg.getConfig().set("general.debug-mode",debugMode);
            plg.saveConfig();
        }
        initMessages();
        saveMessages();
        LNG_CONFIG.debug(Message.values().length,language,true,debugMode);
    }

    /**
     * Enable debugMode
     * @param debug
     */
    public static void setDebugMode (boolean debug){
        debugMode = debug;
    }

    private static boolean copyLanguage(){
        return plugin.saveResource("lang/" +language+".lng",language+".lng",false);
    }

    private static void initMessages(){
        copyLanguage();

        Config lng = null;
        try {
            File f = new File (plugin.getDataFolder()+File.separator+language+".lng");
            lng = new Config(f,Config.YAML);
        } catch (Exception e){
            LNG_LOAD_FAIL.log();
            if (debugMode) e.printStackTrace();
            return;
        }
        for (Message key : Message.values())
            key.initMessage((String) lng.get(key.name().toLowerCase(), key.message));
    }

    private static void saveMessages(){
        File f = new File (plugin.getDataFolder()+File.separator+language+".lng");
        Config lng = new Config(f,Config.YAML);
        for (Message key : Message.values())
            lng.set(key.name().toLowerCase(), key.message);
        try {
            lng.save();
        } catch (Exception e){
            LNG_SAVE_FAIL.log();
            if (debugMode) e.printStackTrace();
            return;
        }
    }

    /**
     * Send message (formed using join method) to server log if debug mode is enabled
     * @param s
     */
    public static boolean debugMessage (Object... s){
        if (debugMode) plugin.getLogger().info(TextFormat.clean(join (s)));
        return true;
    }

    /**
     * Join object array to string (separated by space)
     * @param s
     */
    public static String join (Object... s){
        StringBuilder sb = new StringBuilder();
        for (Object o : s){
            if (sb.length()>0) sb.append(" ");
            sb.append(o.toString());
        }
        return sb.toString();
    }
}