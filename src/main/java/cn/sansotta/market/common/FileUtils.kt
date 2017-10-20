@file:JvmName("FileUtils")

package cn.sansotta.market.common

import cn.sansotta.market.Main
import java.nio.file.Files
import java.nio.file.Paths

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */

fun readFromClasspath(path: String) = Main::class.java.classLoader.getResourceAsStream(path).buffered()

fun readFromFile(path: String) = Files.newInputStream(Paths.get(path)).buffered()
