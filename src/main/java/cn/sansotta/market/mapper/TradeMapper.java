package cn.sansotta.market.mapper;

import org.apache.ibatis.annotations.Arg;
import org.apache.ibatis.annotations.ConstructorArgs;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;

import cn.sansotta.market.domain.entity.TradeEntity;

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Mapper
public interface TradeMapper {
    @Select("SELECT trade_id FROM trades WHERE oid=#{oid}")
    String selectTradeId(@Param("oid") long oid);

    @Results(id = "tradeMap")
    @ConstructorArgs({
            @Arg(id = true, column = "oid", javaType = long.class),
            @Arg(column = "method", javaType = String.class),
            @Arg(column = "create_time", javaType = LocalDateTime.class),
            @Arg(column = "trade_id", javaType = String.class),
            @Arg(column = "extra", javaType = String.class)
    })
    @Select("SELECT * FROM trades WHERE oid=#{oid} FOR UPDATE ")
    TradeEntity selectTradeLocked(@Param("oid") long oid);


    @ResultMap("tradeMap")
    @Select("SELECT * FROM trades WHERE trade_id=#{tradeId} FOR UPDATE ")
    TradeEntity selectTradeByTradeId(@Param("tradeId") String tradeId);

    @ResultMap("tradeMap")
    @Select("SELECT * FROM trades WHERE extra=#{extra}")
    TradeEntity selectTradeByExtra(@Param("extra") String extra);

    @Insert("INSERT INTO trades VALUES (#{oid}, #{method}, #{createTime}, #{tradeId}, #{extra})")
    int insertTrade(TradeEntity trade);

    @Delete("DELETE FROM trades WHERE oid=#{oid}")
    int deleteTrade(long oid);

    @Update("UPDATE trades SET extra=NULL WHERE oid=#{oid}")
    int deleteExtra(@Param("oid") long oid);
}
