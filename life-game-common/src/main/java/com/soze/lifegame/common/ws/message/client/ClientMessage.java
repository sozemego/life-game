package com.soze.lifegame.common.ws.message.client;

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
@JsonSubTypes(value = {
  @JsonSubTypes.Type(value = AuthorizeMessage.class, name = "AUTHORIZE"),
  @JsonSubTypes.Type(value = RequestWorldMessage.class, name = "REQUEST_WORLD"),
  @JsonSubTypes.Type(value = TargetEntity.class, name = "TARGET_ENTITY")
})
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class ClientMessage {

  private final UUID messageId;
  private final String type;

  public ClientMessage(final UUID messageId, final ClientMessageType type) {
    this.messageId = Objects.requireNonNull(messageId);
    this.type = Objects.requireNonNull(type).toString();
  }

  public UUID getMessageId() {
    return messageId;
  }

  public String getType() {
    return type;
  }

  public enum ClientMessageType {
    AUTHORIZE, REQUEST_WORLD, TARGET_ENTITY
  }

}


