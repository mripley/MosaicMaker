def getRects(imgSize, blockSize):
    blockWidth = imgSize[0] / blockSize[0]
    blockHeight = imgSize[1] / blockSize[1]
    
    for x in range(blockSize[0]+1):
        for y in range(blockSize[1]+1):
            start_x = x*blockWidth
            start_y = y*blockHeight
            end_x = min((x+1)*blockWidth, imgSize[0])
            end_y = min((y+1)*blockHeight, imgSize[1])

            width = end_x - start_x
            height = end_y - start_y
            
            print "New Block - Start: (" ,start_x , ",", start_y, ") width: ", width, " height: ", height


getRects((104,200), (20,20))
