export interface GameService {
    start(): Promise<any>
    destroy(): void
}

export interface GameClient {
    connect(): Promise<any>
    send(body: object): void
    authorize(): void
    requestGameWorld(): void
    onMessage(type: string, fn: () => void): () => void
    disconnect(): void
}

export interface Engine {
    start(): void
    setWorld(world: any): void
    stop(): void
    running: boolean
}