package com.gmail.trentech.pjb.utils;

import org.spongepowered.api.Sponge;

import com.gmail.trentech.helpme.help.Argument;
import com.gmail.trentech.helpme.help.Help;
import com.gmail.trentech.helpme.help.Usage;

public class CommandHelp {

	public static void init() {
		if (Sponge.getPluginManager().isLoaded("helpme")) {		
			Usage usageCenter = new Usage(Argument.of("<world>", "Specifies the Targetted world"))
					.addArgument(Argument.of("<x>", "Sets The x-axis center of the world border"))
					.addArgument(Argument.of("<z>", "Sets The z-axis center of the world border"));
			
			Help borderCenter = new Help("border center", "center", "Set the center coordinates of border")
					.setPermission("pjb.cmd.border.center")
					.setUsage(usageCenter)
					.addExample("/border center MyWorld 100 -250");
			
			Usage usageDamage = new Usage(Argument.of("<world>", "Specifies the Targetted world"))
					.addArgument(Argument.of("<disatance>", "Set the distance a player may be be outside the world border before taking damage."))
					.addArgument(Argument.of("[damage]", "Set the damage done to a player per block per tick when outside the buffer."));
			
			Help borderDamage = new Help("border damage", "damage", "Set the damage threshold and amount damage player takes")
					.setPermission("pjb.cmd.border.damage")
					.setUsage(usageDamage)
					.addExample("/border diameter MyWorld 50 2")
					.addExample("/border damage MyWorld 50");
			
			Usage usageDiameter = new Usage(Argument.of("<world>", "Specifies the Targetted world"))
					.addArgument(Argument.of("<startDiameter>", "The diameter where the border will start. "))
					.addArgument(Argument.of("[<time>", "The time over which to change, in milliseconds"))
					.addArgument(Argument.of("[endDiameter]]", "The diameter where the border will end"));
			
			Help borderDiameter = new Help("border diameter", "diameter", "Set the diameter of border. If [endDiameter] is present, the world border diameter will increase/decrease linearly over "
					+ "the specified time. The specified diameter applies to the x and z axis. The world border extends over the entire y-axis.")
					.setPermission("pjb.cmd.border.diameter")
					.setUsage(usageDiameter)
					.addExample("/border diameter MyWorld 5000 120 1000")
					.addExample("/border diameter MyWorld 5000 60")
					.addExample("/border diameter MyWorld 5000");
			
			Usage usageGenerate = new Usage(Argument.of("<world>", "Specifies the Targetted world"))
					.addArgument(Argument.of("[--stop]", "Stops any existing generation task running on this world"))
					.addArgument(Argument.of("[--verbose]", "Displays chunk generation infomation in the console"))
					.addArgument(Argument.of("[-i <tickInverval>]", "Sets the interval between generation runs. Must be greater than 0. Default is 10."))
					.addArgument(Argument.of("[-p <tickPercent>]", "Sets the limit of tick time that can be used to generate chunks as a percentage of tickInterval. The percentage should "
							+ "be a value in the range (0, 1]. No estimation is used to decide when to stop so the actual value will always be somewhere above the given percentage Default is 0.15."))
					.addArgument(Argument.of("[-c <chunkCount>]", "Sets maximum number of chunks per tick to generate. Use a value smaller or equal to 0 to disable. Default is disabled."));
					
			Help borderGenerate = new Help("border generate", "generate", "Pre generate chunks inside border")
					.setPermission("pjb.cmd.border.generate")
					.setUsage(usageGenerate)
					.addExample("/border generate MyWorld --verbose -i 60 -p 0.4 -c 5")
					.addExample("/border generate MyWorld --stop")
					.addExample("/border generate MyWorld");
			
			Usage usageInfo = new Usage(Argument.of("<world>", "Specifies the Targetted world"));
			
			Help borderInfo = new Help("border info", "info", "Show information of world border")
					.setPermission("pjb.cmd.border.info")
					.setUsage(usageInfo)
					.addExample("/border info MyWorld");
			
			Usage usageWarning = new Usage(Argument.of("<world>", "Specifies the Targetted world"))
					.addArgument(Argument.of("<distance>", "Set the distance when a contracting world border will warn a player for whom the world border is distance blocks away."))
					.addArgument(Argument.of("[time]", "Set the time when a contracting world border will warn a player for whom the world border will reach in time seconds."));
			
			Help borderWarning = new Help("border warning", "warning", "Set warning distance and time. The warning is displayed in the form of a reddish tint in game.")
					.setPermission("pjb.cmd.border.warning")
					.setUsage(usageWarning)
					.addExample("/border warning MyWorld 4900 10")
					.addExample("/border warning MyWorld 4900");
			
			Help border = new Help("border", "border", "Base Project Borders command")
					.setPermission("pji.cmd.border")
					.addChild(borderWarning)
					.addChild(borderInfo)
					.addChild(borderGenerate)
					.addChild(borderDiameter)
					.addChild(borderDamage)
					.addChild(borderCenter);
			
			Help.register(border);
		}
	}
}
