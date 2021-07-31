package com.pedro.rtplibrary.rtmp;

import android.content.Context;
import android.media.MediaCodec;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.view.SurfaceView;
import android.view.TextureView;

import com.pedro.rtplibrary.base.Camera2Base;

import com.pedro.rtplibrary.view.LightOpenGlView;
import com.pedro.rtplibrary.view.OpenGlView;
import net.ossrs.rtmp.ConnectCheckerRtmp;
import net.ossrs.rtmp.SrsFlvMuxer;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * More documentation see:
 * {@link com.pedro.rtplibrary.base.Camera2Base}
 *
 * Created by pedro on 6/07/17.
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class RtmpCamera2 extends Camera2Base {

  private SrsFlvMuxer srsFlvMuxer;
  private SrsFlvMuxer srsFlvMuxer1;
  /**
   * @deprecated This view produce rotations problems and could be unsupported in future versions.
   * Use {@link Camera2Base#Camera2Base(OpenGlView)} or {@link Camera2Base#Camera2Base(LightOpenGlView)}
   * instead.
   */
  @Deprecated
  public RtmpCamera2(SurfaceView surfaceView, ConnectCheckerRtmp connectChecker) {
    super(surfaceView);
    srsFlvMuxer = new SrsFlvMuxer(connectChecker);
    srsFlvMuxer1 = new SrsFlvMuxer(connectChecker);
  }

  /**
   * @deprecated This view produce rotations problems and could be unsupported in future versions.
   * Use {@link Camera2Base#Camera2Base(OpenGlView)} or {@link Camera2Base#Camera2Base(LightOpenGlView)}
   * instead.
   */
  @Deprecated
  public RtmpCamera2(TextureView textureView, ConnectCheckerRtmp connectChecker) {
    super(textureView);
    srsFlvMuxer = new SrsFlvMuxer(connectChecker);
    srsFlvMuxer1 = new SrsFlvMuxer(connectChecker);

  }

  public RtmpCamera2(OpenGlView openGlView, ConnectCheckerRtmp connectChecker) {
    super(openGlView);
    srsFlvMuxer = new SrsFlvMuxer(connectChecker);
    srsFlvMuxer1 = new SrsFlvMuxer(connectChecker);

  }

  public RtmpCamera2(LightOpenGlView lightOpenGlView, ConnectCheckerRtmp connectChecker) {
    super(lightOpenGlView);
    srsFlvMuxer = new SrsFlvMuxer(connectChecker);
    srsFlvMuxer1 = new SrsFlvMuxer(connectChecker);

  }

  public RtmpCamera2(Context context, boolean useOpengl, ConnectCheckerRtmp connectChecker) {
    super(context, useOpengl);
    srsFlvMuxer = new SrsFlvMuxer(connectChecker);
    srsFlvMuxer1 = new SrsFlvMuxer(connectChecker);

  }

  /**
   * H264 profile.
   *
   * @param profileIop Could be ProfileIop.BASELINE or ProfileIop.CONSTRAINED
   */
  public void setProfileIop(byte profileIop) {
    srsFlvMuxer.setProfileIop(profileIop);
    srsFlvMuxer1.setProfileIop(profileIop);
  }

  @Override
  public void resizeCache(int newSize) throws RuntimeException {
    srsFlvMuxer.resizeFlvTagCache(newSize);
    srsFlvMuxer1.resizeFlvTagCache(newSize);
  }

  @Override
  public int getCacheSize() {
    return srsFlvMuxer.getFlvTagCacheSize();
  }

  @Override
  public long getSentAudioFrames() {
    return srsFlvMuxer.getSentAudioFrames() + srsFlvMuxer1.getSentAudioFrames();
  }

  @Override
  public long getSentVideoFrames() {
    return srsFlvMuxer.getSentVideoFrames() + srsFlvMuxer1.getSentVideoFrames();
  }

  @Override
  public long getDroppedAudioFrames() {
    return srsFlvMuxer.getDroppedAudioFrames() + srsFlvMuxer1.getDroppedAudioFrames();
  }

  @Override
  public long getDroppedVideoFrames() {
    return srsFlvMuxer.getDroppedVideoFrames() + srsFlvMuxer1.getDroppedVideoFrames();
  }

  @Override
  public void resetSentAudioFrames() {
    srsFlvMuxer.resetSentAudioFrames();
    srsFlvMuxer1.resetSentAudioFrames();
  }

  @Override
  public void resetSentVideoFrames() {
    srsFlvMuxer.resetSentVideoFrames();
    srsFlvMuxer1.resetSentVideoFrames();
  }

  @Override
  public void resetDroppedAudioFrames() {
    srsFlvMuxer.resetDroppedAudioFrames();
    srsFlvMuxer1.resetDroppedAudioFrames();
  }

  @Override
  public void resetDroppedVideoFrames() {
    srsFlvMuxer.resetDroppedVideoFrames();
    srsFlvMuxer1.resetDroppedVideoFrames();
  }

  @Override
  public void setAuthorization(String user, String password) {
    srsFlvMuxer.setAuthorization(user, password);
  }

  /**
   * Some Livestream hosts use Akamai auth that requires RTMP packets to be sent with increasing
   * timestamp order regardless of packet type.
   * Necessary with Servers like Dacast.
   * More info here:
   * https://learn.akamai.com/en-us/webhelp/media-services-live/media-services-live-encoder-compatibility-testing-and-qualification-guide-v4.0/GUID-F941C88B-9128-4BF4-A81B-C2E5CFD35BBF.html
   */
  public void forceAkamaiTs(boolean enabled) {
    srsFlvMuxer.forceAkamaiTs(enabled);
  }

  @Override
  protected void prepareAudioRtp(boolean isStereo, int sampleRate) {
    srsFlvMuxer.setIsStereo(isStereo);
    srsFlvMuxer.setSampleRate(sampleRate);
    srsFlvMuxer1.setIsStereo(isStereo);
    srsFlvMuxer1.setSampleRate(sampleRate);
  }

  @Override
  protected void startStreamRtp(List<String> url) {
    if (videoEncoder.getRotation() == 90 || videoEncoder.getRotation() == 270) {
      srsFlvMuxer.setVideoResolution(videoEncoder.getHeight(), videoEncoder.getWidth());
      srsFlvMuxer1.setVideoResolution(videoEncoder.getHeight(), videoEncoder.getWidth());
    } else {
      srsFlvMuxer.setVideoResolution(videoEncoder.getWidth(), videoEncoder.getHeight());
      srsFlvMuxer1.setVideoResolution(videoEncoder.getWidth(), videoEncoder.getHeight());
    }
    srsFlvMuxer.start(url.get(0));
    srsFlvMuxer1.start(url.get(1));
  }

  @Override
  protected void stopStreamRtp() {
    srsFlvMuxer.stop();
    srsFlvMuxer1.stop();
  }

  @Override
  public void setReTries(int reTries) {
    srsFlvMuxer.setReTries(reTries);
    srsFlvMuxer1.setReTries(reTries);
  }

  @Override
  public boolean shouldRetry(String reason) {
    return srsFlvMuxer.shouldRetry(reason);
  }

  @Override
  public void reConnect(long delay) {
    srsFlvMuxer.reConnect(delay);
    srsFlvMuxer1.reConnect(delay);
  }

  @Override
  public boolean hasCongestion() {
    return false;
  }

  @Override
  protected void getAacDataRtp(ByteBuffer aacBuffer, MediaCodec.BufferInfo info) {
    srsFlvMuxer.sendAudio(aacBuffer.duplicate(), info);
    srsFlvMuxer1.sendAudio(aacBuffer, info);
  }

  @Override
  protected void onSpsPpsVpsRtp(ByteBuffer sps, ByteBuffer pps, ByteBuffer vps) {
    srsFlvMuxer.setSpsPPs(sps.duplicate(), pps.duplicate());
    srsFlvMuxer1.setSpsPPs(sps, pps);
  }

  @Override
  protected void getH264DataRtp(ByteBuffer h264Buffer, MediaCodec.BufferInfo info) {
    srsFlvMuxer.sendVideo(h264Buffer.duplicate(), info);
    srsFlvMuxer1.sendVideo(h264Buffer, info);
  }


  @Override
  public void setLogs(boolean enable) {
    srsFlvMuxer.setLogs(enable);
    srsFlvMuxer1.setLogs(enable);
  }
}

