/*
 *  Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */

package org.wso2.carbon.esb.websocket.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.SslContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Simple WebSocket server for Test cases.
 */
public final class WebSocketServer {

    private static final Log logger = LogFactory.getLog(WebSocketServer.class);

    private final int port;
    private String subProtocols = null;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    public WebSocketServer(int port) {
        this.port = port;
    }

    public WebSocketServer(int port, String subProtocols) {
        this.port = port;
        this.subProtocols = subProtocols;
    }

    public void run() throws InterruptedException {
        final SslContext sslCtx = null;
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup(2);

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                .childHandler(new WebSocketRemoteServerInitializer(sslCtx, subProtocols));

        serverBootstrap.bind(port).sync();
        logger.info("WebSocket remote server started listening on port " + port);
    }

    public void stop() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        logger.info("WebSocket remote server stopped listening  on port " + port);
    }
}
