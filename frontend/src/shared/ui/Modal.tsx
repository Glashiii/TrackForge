import {
    Dialog,
    DialogBackdrop,
    DialogPanel,
    DialogTitle,
} from '@headlessui/react';
import type { ReactNode } from 'react';

type ModalProps = {
    open: boolean;
    onClose: () => void;
    title: string;
    children: ReactNode;
};

export default function Modal({open, onClose, title, children,}: ModalProps) {
    return (
        <Dialog open={open} onClose={onClose} className="relative z-50">
            <DialogBackdrop className="fixed inset-0 bg-black/40" />

            <div className="fixed inset-0 overflow-y-auto">
                <div className="flex min-h-full items-center justify-center p-4">
                    <DialogPanel className="w-full max-w-md rounded-xl bg-white p-6">
                        <div className="flex justify-between">
                            <DialogTitle className="text-xl font-bold">
                                {title}
                            </DialogTitle>

                            <button type="button" onClick={onClose}>
                                X
                            </button>
                        </div>

                        {children}
                    </DialogPanel>
                </div>
            </div>
        </Dialog>
    );
}