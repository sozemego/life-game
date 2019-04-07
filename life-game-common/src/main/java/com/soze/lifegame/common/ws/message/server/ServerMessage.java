package com.soze.lifegame.common.ws.message.server;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Objects;
import java.util.UUID;

@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.EXISTING_PROPERTY,
  property = "type",
  visible = true
)
@JsonSubTypes( value = {
  @JsonSubTypes.Type(value = AuthorizedMessage.class, name = "AUTHORIZED"),
  @JsonSubTypes.Type(value = WorldMessage.class, name = "WORLD"),
  @JsonSubTypes.Type(value = EntityMessage.class, name = "ENTITY"),
  @JsonSubTypes.Type(value = EntityPositionMessage.class, name = "ENTITY_POSITION")
}
)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class ServerMessage {

  private final UUID messageId;
  private final String type;

  public ServerMessage(final UUID messageId, final ServerMessageType type) {
    this.messageId = Objects.requireNonNull(messageId);
    this.type = Objects.requireNonNull(type).toString();
  }

  public UUID getMessageId() {
    return messageId;
  }

  public String getType() {
    return type;
  }

  public enum ServerMessageType {
    AUTHORIZED, WORLD, ENTITY, ENTITY_POSITION
  }

}


