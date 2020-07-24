/*
 * rhsso
 * rhsso
 *
 * OpenAPI spec version: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package com.tsystems.tm.acc.wiremock.oauth.client.model;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.annotations.ApiModelProperty;

import java.io.IOException;
import java.util.Objects;

/**
 * Token
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2020-02-07T17:04:29.119+03:00")
public class Token {
  @SerializedName("access_token")
  private String accessToken = null;

  @SerializedName("refresh_token")
  private String refreshToken = null;

  @SerializedName("id_token")
  private String idToken = null;

  @SerializedName("expires_in")
  private Integer expiresIn = null;

  @SerializedName("refresh_expires_in")
  private Integer refreshExpiresIn = null;

  /**
   * Gets or Sets tokenType
   */
  @JsonAdapter(TokenTypeEnum.Adapter.class)
  public enum TokenTypeEnum {
    BEARER("bearer");

    private String value;

    TokenTypeEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static TokenTypeEnum fromValue(String text) {
      for (TokenTypeEnum b : TokenTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

    public static class Adapter extends TypeAdapter<TokenTypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final TokenTypeEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public TokenTypeEnum read(final JsonReader jsonReader) throws IOException {
        String value = jsonReader.nextString();
        return TokenTypeEnum.fromValue(String.valueOf(value));
      }
    }
  }

  @SerializedName("token_type")
  private TokenTypeEnum tokenType = null;

  @SerializedName("session_state")
  private String sessionState = null;

  @SerializedName("not-before-policy")
  private Integer notBeforePolicy = null;

  @SerializedName("scope")
  private String scope = null;

  public Token accessToken(String accessToken) {
    this.accessToken = accessToken;
    return this;
  }

   /**
   * Get accessToken
   * @return accessToken
  **/
  @ApiModelProperty(value = "")
  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public Token refreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
    return this;
  }

   /**
   * Get refreshToken
   * @return refreshToken
  **/
  @ApiModelProperty(value = "")
  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public Token idToken(String idToken) {
    this.idToken = idToken;
    return this;
  }

   /**
   * Get idToken
   * @return idToken
  **/
  @ApiModelProperty(value = "")
  public String getIdToken() {
    return idToken;
  }

  public void setIdToken(String idToken) {
    this.idToken = idToken;
  }

  public Token expiresIn(Integer expiresIn) {
    this.expiresIn = expiresIn;
    return this;
  }

   /**
   * Get expiresIn
   * @return expiresIn
  **/
  @ApiModelProperty(value = "")
  public Integer getExpiresIn() {
    return expiresIn;
  }

  public void setExpiresIn(Integer expiresIn) {
    this.expiresIn = expiresIn;
  }

  public Token refreshExpiresIn(Integer refreshExpiresIn) {
    this.refreshExpiresIn = refreshExpiresIn;
    return this;
  }

   /**
   * Get refreshExpiresIn
   * @return refreshExpiresIn
  **/
  @ApiModelProperty(value = "")
  public Integer getRefreshExpiresIn() {
    return refreshExpiresIn;
  }

  public void setRefreshExpiresIn(Integer refreshExpiresIn) {
    this.refreshExpiresIn = refreshExpiresIn;
  }

  public Token tokenType(TokenTypeEnum tokenType) {
    this.tokenType = tokenType;
    return this;
  }

   /**
   * Get tokenType
   * @return tokenType
  **/
  @ApiModelProperty(value = "")
  public TokenTypeEnum getTokenType() {
    return tokenType;
  }

  public void setTokenType(TokenTypeEnum tokenType) {
    this.tokenType = tokenType;
  }

  public Token sessionState(String sessionState) {
    this.sessionState = sessionState;
    return this;
  }

   /**
   * Get sessionState
   * @return sessionState
  **/
  @ApiModelProperty(value = "")
  public String getSessionState() {
    return sessionState;
  }

  public void setSessionState(String sessionState) {
    this.sessionState = sessionState;
  }

  public Token notBeforePolicy(Integer notBeforePolicy) {
    this.notBeforePolicy = notBeforePolicy;
    return this;
  }

   /**
   * Get notBeforePolicy
   * @return notBeforePolicy
  **/
  @ApiModelProperty(value = "")
  public Integer getNotBeforePolicy() {
    return notBeforePolicy;
  }

  public void setNotBeforePolicy(Integer notBeforePolicy) {
    this.notBeforePolicy = notBeforePolicy;
  }

  public Token scope(String scope) {
    this.scope = scope;
    return this;
  }

   /**
   * Get scope
   * @return scope
  **/
  @ApiModelProperty(value = "")
  public String getScope() {
    return scope;
  }

  public void setScope(String scope) {
    this.scope = scope;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Token token = (Token) o;
    return Objects.equals(this.accessToken, token.accessToken) &&
        Objects.equals(this.refreshToken, token.refreshToken) &&
        Objects.equals(this.idToken, token.idToken) &&
        Objects.equals(this.expiresIn, token.expiresIn) &&
        Objects.equals(this.refreshExpiresIn, token.refreshExpiresIn) &&
        Objects.equals(this.tokenType, token.tokenType) &&
        Objects.equals(this.sessionState, token.sessionState) &&
        Objects.equals(this.notBeforePolicy, token.notBeforePolicy) &&
        Objects.equals(this.scope, token.scope);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accessToken, refreshToken, idToken, expiresIn, refreshExpiresIn, tokenType, sessionState, notBeforePolicy, scope);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Token {\n");

    sb.append("    accessToken: ").append(toIndentedString(accessToken)).append("\n");
    sb.append("    refreshToken: ").append(toIndentedString(refreshToken)).append("\n");
    sb.append("    idToken: ").append(toIndentedString(idToken)).append("\n");
    sb.append("    expiresIn: ").append(toIndentedString(expiresIn)).append("\n");
    sb.append("    refreshExpiresIn: ").append(toIndentedString(refreshExpiresIn)).append("\n");
    sb.append("    tokenType: ").append(toIndentedString(tokenType)).append("\n");
    sb.append("    sessionState: ").append(toIndentedString(sessionState)).append("\n");
    sb.append("    notBeforePolicy: ").append(toIndentedString(notBeforePolicy)).append("\n");
    sb.append("    scope: ").append(toIndentedString(scope)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}

