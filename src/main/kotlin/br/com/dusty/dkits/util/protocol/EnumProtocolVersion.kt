package br.com.dusty.dkits.util.protocol

enum class EnumProtocolVersion {

	UNKNOWN(-1, "unknown", EnumProtocolSafety.TERRIBLE),
	PRE_1_7(0, 3, "pre-1.7", EnumProtocolSafety.TERRIBLE),
	RELEASE_1_7_2(4, "1.7.2-1.7.5", EnumProtocolSafety.BAD),
	RELEASE_1_7_10(5, "1.7.6-1.7.10", EnumProtocolSafety.GOOD),
	SNAPSHOT_1_8(6, 46, "snapshot_1.8", EnumProtocolSafety.TERRIBLE),
	RELEASE_1_8(47, "1.8.0-1.8.9", EnumProtocolSafety.REGULAR),
	SNAPSHOT_1_9(48, 106, "snapshot_1.9", EnumProtocolSafety.TERRIBLE),
	RELEASE_1_9(107, "1.9.0", EnumProtocolSafety.TERRIBLE),
	RELEASE_1_9_1(108, "1.9.1", EnumProtocolSafety.TERRIBLE),
	RELEASE_1_9_2(109, "1.9.2", EnumProtocolSafety.REGULAR),
	RELEASE_1_9_4(110, "1.9.3-1.9.4", EnumProtocolSafety.GOOD),
	SNAPSHOT_1_10(201, 205, "snapshot_1.10", EnumProtocolSafety.TERRIBLE),
	RELEASE_1_10(210, "1.10.0-1.10.2", EnumProtocolSafety.GOOD),
	SNAPSHOT_1_11(301, 314, "snapshot_1.11", EnumProtocolSafety.TERRIBLE),
	RELEASE_1_11(315, "1.11.0", EnumProtocolSafety.REGULAR),
	RELEASE_1_11_2(316, "1.11.1-1.11.2", EnumProtocolSafety.GOOD),
	SNAPSHOT_1_12(317, 334, "snapshot_1.12", EnumProtocolSafety.TERRIBLE),
	RELEASE_1_12(335, "1.12", EnumProtocolSafety.EXCELLENT),
	SNAPSHOT_1_12_1(336, 337, "snapshot_1.12.1", EnumProtocolSafety.TERRIBLE),
	RELEASE_1_12_1(338, "1.12.1", EnumProtocolSafety.BAD),
	SNAPSHOT_1_12_2(339, 339, "snapshot_1.12.2", EnumProtocolSafety.TERRIBLE),
	RELEASE_1_12_2(340, "1.12.2", EnumProtocolSafety.EXCELLENT);

	var min: Int = 0
	var max: Int = 0
	var string = ""
	var safety = EnumProtocolSafety.TERRIBLE

	constructor(version: Int, string: String, safety: EnumProtocolSafety) {
		this.min = version
		this.max = version
		this.string = safety.color.toString() + string
		this.safety = safety
	}

	constructor(min: Int, max: Int, string: String, safety: EnumProtocolSafety) {
		this.min = min
		this.max = max
		this.string = safety.color.toString() + string
		this.safety = safety
	}

	fun isGreaterThan(protocolVersion: EnumProtocolVersion) = protocolVersion.max < min

	fun isGreaterThanOrEquals(protocolVersion: EnumProtocolVersion) = this == protocolVersion || protocolVersion.max < min

	fun isLowerThan(protocolVersion: EnumProtocolVersion) = protocolVersion.min > max

	fun isLowerThanOrEquals(protocolVersion: EnumProtocolVersion) = this == protocolVersion || protocolVersion.min > max

	override fun toString(): String = string

	companion object {

		operator fun get(version: Int?) = if (version == null) UNKNOWN else (values().firstOrNull { it.min <= version && it.max >= version } ?: UNKNOWN)
	}
}
