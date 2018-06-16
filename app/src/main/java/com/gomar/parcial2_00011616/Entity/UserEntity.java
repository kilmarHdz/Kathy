package com.gomar.parcial2_00011616.Entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "table_user")

public class UserEntity {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String _id;
    @ColumnInfo(name = "username")
    private String username;
    @ColumnInfo(name = "avatar")
    private String avatar ="http://noAvatar.jpg";
    @ColumnInfo(name = "password")
    private String password;
    @ColumnInfo(name = "create_date")
    private String createDate;

    @Ignore
    public UserEntity() {
    }

    public UserEntity(String _id, @NonNull String username, @NonNull String avatar, @NonNull String password, @NonNull String createDate) {
        this._id = _id;
        this.username = username;
        this.avatar = avatar;
        this.password = password;
        this.createDate = createDate;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    public void setAvatar(@NonNull String avatar) {
        this.avatar = avatar;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }

    public void setCreateDate(@NonNull String createDate) {
        this.createDate = createDate;
    }


    public String get_id() {
        return _id;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    @NonNull
    public String getAvatar() {
        return avatar;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    @NonNull
    public String getCreateDate() {
        return createDate;
    }


    @Override
    public String toString() {
        return "User{" +
                "_id='" + _id + '\'' +
                ", username='" + username + '\'' +
                ", avatar='" + avatar + '\'' +
                ", password='" + password + '\'' +
                ", createDate='" + createDate + '\'' +
                '}';
    }

    public static class Token implements Parcelable {

        @SerializedName("token")
        private String tokenSecurity;

        protected Token(Parcel in) {
            tokenSecurity = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(tokenSecurity);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Token> CREATOR = new Creator<Token>() {
            @Override
            public Token createFromParcel(Parcel in) {
                return new Token(in);
            }

            @Override
            public Token[] newArray(int size) {
                return new Token[size];
            }
        };

        public Token(String tokenSecurity) {
            this.tokenSecurity = tokenSecurity;
        }

        public String getTokenSecurity() {
            return tokenSecurity;
        }

        public void setTokenSecurity(String tokenSecurity) {
            this.tokenSecurity = tokenSecurity;
        }
    }
}
