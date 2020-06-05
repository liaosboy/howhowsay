from pypinyin import pinyin, Style
from moviepy.editor import *
import pygame
import pandas as pd
import socket


def pro(text):
    t = pinyin(text, style=Style.BOPOMOFO)
    t = t[0][0]
    return t


def make_video(text):

    text = [pro(t) for t in text]
    print(text)
    table = pd.read_excel('voice_table.xlsx')
    table = pd.DataFrame(table)

    video = VideoFileClip('HowFun.mp4')
    unique_index = pd.Index(list(table['word']))
    clip = []

    for t in text:
        try:
            i = unique_index.get_loc(t)
            start = table['start'][i]
            end = table['end'][i]
            print(start)
            print(end)
            clip.append(video.subclip(start, end))
        except:
            print("無法找到%s" % t)

    try:
        out = concatenate_videoclips([c for c in clip])
        out.write_videofile('test.mp4')
    except:
        print("匯出失敗")


host = '192.168.100.102'  # 對server端為主機位置
port = 5555
# host = socket.gethostname()
# port = 5000
address = (host, port)

socket01 = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
# AF_INET:默認IPv4, SOCK_STREAM:TCP

socket01.bind(address)  # 讓這個socket要綁到位址(ip/port)
socket01.listen(3)  # listen(backlog)
# backlog:操作系統可以掛起的最大連接數量。該值至少為1，大部分應用程序設為5就可以了
print('Socket Startup')
while True:
    conn, addr = socket01.accept()  # 接受遠程計算機的連接請求，建立起與客戶機之間的通信連接
    # 返回（conn,address)
    # conn是新的套接字對象，可以用來接收和發送數據。address是連接客戶端的地址
    print('Connected by', addr)

    ##################################
    # 開始傳輸
    text = str(conn.recv(1024), encoding='utf-8')
    make_video(text)

    print('start send image')
    imgFile = open("test.mp4", "rb")
    while True:
        imgData = imgFile.readline(1024)
        if not imgData:
            break  # 讀完檔案結束迴圈

        conn.send(imgData)
    imgFile.close()
    print('transmit end')
    conn.close()  # 關閉
    ##################################


socket01.close()
print('server close')
