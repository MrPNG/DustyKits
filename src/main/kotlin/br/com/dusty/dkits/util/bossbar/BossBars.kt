package br.com.dusty.dkits.util.bossbar

import br.com.dusty.dkits.util.protocol.Protocols
import br.com.dusty.dkits.util.text.Text
import br.com.dusty.dkits.util.text.TextColor
import br.com.dusty.dkits.util.text.TextStyle
import us.myles.ViaVersion.api.boss.BossColor
import us.myles.ViaVersion.api.boss.BossStyle

object BossBars {

	val MAIN = Protocols.VIA_VERSION_API.createBossBar(Text.of("Dusty").color(TextColor.GOLD).styles(TextStyle.BOLD).append(" - ").color(TextColor.WHITE).append("dusty.com.br").color(TextColor.GREEN).styles(
			TextStyle.BOLD).toString(), 1f, BossColor.RED, BossStyle.SEGMENTED_20)
}
