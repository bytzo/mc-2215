package net.bytzo.mc_2215.mixins;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.server.dedicated.Settings;

@Mixin(value = Settings.class, priority = 2000)
public class SettingsMixin {
	@Unique
	private static final Charset CHARSET = StandardCharsets.UTF_8;

	@Redirect(
			method = "loadFromFile(Ljava/nio/file/Path;)Ljava/util/Properties;",
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/Properties;load(Ljava/io/InputStream;)V"
			)
	)
	private static void load(Properties properties, InputStream inStream) throws IOException {
		properties.load(new InputStreamReader(inStream, CHARSET));
	}
	
	@Redirect(
			method = "store(Ljava/nio/file/Path;)V",
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/Properties;store(Ljava/io/OutputStream;Ljava/lang/String;)V"
			)
	)
	private void store(Properties properties, OutputStream out, String comments) throws IOException {
		properties.store(new OutputStreamWriter(out, CHARSET), comments);
	}
}
